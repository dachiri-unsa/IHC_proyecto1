package com.example.ihc_proyecto1.ui.screens.flujo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihc_proyecto1.data.local.FotoStore
import com.example.ihc_proyecto1.data.model.AulaOpcion
import com.example.ihc_proyecto1.data.model.BorradorReporte
import com.example.ihc_proyecto1.data.model.Catalogos
import com.example.ihc_proyecto1.data.model.ResultadoEnvio
import com.example.ihc_proyecto1.data.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario mientras el usuario reporta un objeto. */
data class BorradorUiState(
    val fotoPath: String? = null,
    val categoria: String? = null,
    val tipo: String? = null,
    val carrera: String? = null,
    val aula: String? = null,
    /** El aula elegida es del laboratorio de la carrera. */
    val esLaboratorio: Boolean = false,
    /** El usuario eligió escribir el aula en vez de tomarla del catálogo. */
    val aulaPersonalizada: Boolean = false,
    val nota: String = "",
    val enviando: Boolean = false,
    val enviadoOnline: Boolean = false,
    val pin: String? = null,
    val reporteId: String? = null,
    val hayConexion: Boolean = true,
    val mostrarErrores: Boolean = false
) {
    val destinoVigilancia: String get() = Catalogos.destinoVigilancia(carrera.orEmpty())
    val tiposDisponibles: List<String> get() = Catalogos.tiposDe(categoria)

    /** Aulas de la carrera elegida (aulas + las del único laboratorio). */
    val aulasDisponibles: List<AulaOpcion> get() = Catalogos.aulasDe(carrera)

    val laboratorioDeLaCarrera: String? get() = Catalogos.laboratorioDe(carrera)

    /** El nombre del laboratorio al que pertenece el aula elegida. */
    val nombreLaboratorio: String?
        get() = if (esLaboratorio) laboratorioDeLaCarrera else null

    private val aulaLlena: Boolean get() = !aula.isNullOrBlank()

    val datosCompletos: Boolean
        get() = categoria != null && tipo != null && carrera != null && aulaLlena

    fun errorDe(etiqueta: String): String? = when {
        !mostrarErrores -> null
        etiqueta == "Categoría" && categoria == null -> "Elige una categoría"
        etiqueta == "Tipo" && tipo == null -> "Elige un tipo"
        etiqueta == "Carrera" && carrera == null -> "Elige la carrera"
        etiqueta == "Aula" && carrera == null -> "Elige primero la carrera"
        etiqueta == "Aula" && !aulaLlena ->
            if (aulaPersonalizada) "Escribe el nombre del aula" else "Elige el aula donde lo encontraste"

        else -> null
    }

    fun aBorrador(): BorradorReporte? {
        if (fotoPath == null || !datosCompletos) return null
        return BorradorReporte(
            fotoPath = fotoPath,
            categoria = categoria.orEmpty(),
            tipo = tipo.orEmpty(),
            carrera = carrera.orEmpty(),
            aula = aula.orEmpty(),
            esLaboratorio = esLaboratorio,
            nota = nota.ifBlank { null },
            destinoVigilancia = destinoVigilancia
        )
    }
}

class ReportarViewModel(
    private val repositorio: ReporteRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(BorradorUiState())
    val estado: StateFlow<BorradorUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repositorio.conexionActual.collect { conexion ->
                _estado.update { it.copy(hayConexion = conexion) }
            }
        }
    }

    fun fotoCapturada(ruta: String) {
        _estado.update { it.copy(fotoPath = ruta) }
    }

    fun descartarFoto() {
        FotoStore.eliminar(_estado.value.fotoPath)
        _estado.update { it.copy(fotoPath = null) }
    }

    fun cambiarCategoria(valor: String) {
        _estado.update {
            val sigueValido = it.tipo in Catalogos.tiposDe(valor)
            it.copy(categoria = valor, tipo = if (sigueValido) it.tipo else null)
        }
    }

    fun cambiarTipo(valor: String) = _estado.update { it.copy(tipo = valor) }

    fun cambiarCarrera(valor: String) = _estado.update {
        // Las aulas dependen de la carrera, así que se limpian al cambiarla.
        it.copy(carrera = valor, aula = null, esLaboratorio = false, aulaPersonalizada = false)
    }

    /** Elige un aula del catálogo de la carrera (puede ser del laboratorio). */
    fun elegirAula(opcion: AulaOpcion) = _estado.update {
        it.copy(
            aula = opcion.nombre,
            esLaboratorio = opcion.esLaboratorio,
            aulaPersonalizada = false
        )
    }

    /** El usuario decide escribir el aula porque no está en el catálogo. */
    fun usarAulaPersonalizada() = _estado.update {
        it.copy(aulaPersonalizada = true, aula = "", esLaboratorio = false)
    }

    fun cambiarAula(valor: String) = _estado.update {
        it.copy(aula = valor.take(60), esLaboratorio = false)
    }

    fun cambiarNota(valor: String) = _estado.update {
        it.copy(nota = valor.take(200))
    }

    /** Valida los campos obligatorios; devuelve true si se puede continuar. */
    fun validar(): Boolean {
        val completo = _estado.value.datosCompletos
        _estado.update { it.copy(mostrarErrores = !completo) }
        return completo
    }

    /** Envía el reporte. Notifica el resultado para que la UI navegue. */
    fun enviar(alTerminar: (ResultadoEnvio) -> Unit) {
        val borrador = _estado.value.aBorrador()
        if (borrador == null) {
            _estado.update { it.copy(mostrarErrores = true) }
            return
        }
        if (_estado.value.enviando) return

        _estado.update { it.copy(enviando = true) }
        viewModelScope.launch {
            val resultado = repositorio.registrarReporte(borrador)
            _estado.update {
                when (resultado) {
                    is ResultadoEnvio.Enviado -> it.copy(
                        enviando = false,
                        enviadoOnline = true,
                        pin = resultado.pin
                    )

                    is ResultadoEnvio.GuardadoLocal -> it.copy(
                        enviando = false,
                        enviadoOnline = false,
                        pin = resultado.pin
                    )
                }
            }
            alTerminar(resultado)
        }
    }

    fun marcarReporteCreado(id: String) = _estado.update { it.copy(reporteId = id) }

    /**
     * Limpia el borrador al terminar el flujo. OJO: aquí no se borra la foto,
     * porque ya quedó guardada en el reporte enviado.
     */
    fun reiniciar() {
        _estado.value = BorradorUiState()
    }
}