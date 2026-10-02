package com.example.ihc_proyecto1.data.repository

import com.example.ihc_proyecto1.data.local.ReporteDao
import com.example.ihc_proyecto1.data.local.ReporteEntity
import com.example.ihc_proyecto1.data.model.BorradorReporte
import com.example.ihc_proyecto1.data.model.Reporte
import com.example.ihc_proyecto1.data.model.ResultadoEnvio
import com.example.ihc_proyecto1.data.network.ConectividadRepository
import com.example.ihc_proyecto1.data.remote.ReporteRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class ReporteRepository(
    private val dao: ReporteDao,
    private val remoto: ReporteRemoteDataSource,
    private val conectividad: ConectividadRepository,
    private val scope: CoroutineScope
) {
    /** Marca visual de "sincronizando..." para las pantallas. */
    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando

    val reportes: Flow<List<Reporte>> =
        dao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    val pendientes: Flow<Int> = dao.observarPendientes()

    /** Conexión efectiva que ve la app (incluye el modo sin conexión de prueba). */
    val conexionActual: StateFlow<Boolean> = conectividad.hayConexion

    fun reporte(id: String): Flow<Reporte?> =
        dao.observarPorId(id).map { it?.toDomain() }

    /**
     * Guarda el reporte localmente (con PIN temporal) y luego intenta subirlo.
     * Siempre queda en la base: si no hay red, se sincroniza después.
     */
    suspend fun registrarReporte(borrador: BorradorReporte): ResultadoEnvio {
        val id = UUID.randomUUID().toString()
        val pinTemporal = generarPinUnico()
        val ahora = System.currentTimeMillis()

        dao.insertar(
            ReporteEntity(
                id = id,
                fotoPath = borrador.fotoPath,
                categoria = borrador.categoria,
                tipo = borrador.tipo,
                carrera = borrador.carrera,
                aula = borrador.aula,
                esLaboratorio = borrador.esLaboratorio,
                nota = borrador.nota.orEmpty(),
                destinoVigilancia = borrador.destinoVigilancia,
                estado = "PENDIENTE",
                pin = pinTemporal,
                pinTemporal = true,
                creadoEn = ahora,
                sincronizadoEn = null
            )
        )

        val respuesta = remoto.enviar(borrador, conectividad.hayConexion.value)
        return when (respuesta) {
            is ReporteRemoteDataSource.Respuesta.Aceptado -> {
                val pinOnline = generarPinUnico()
                dao.marcarEnLinea(id, pinOnline, System.currentTimeMillis())
                ResultadoEnvio.Enviado(pinOnline)
            }

            is ReporteRemoteDataSource.Respuesta.SinConexion -> {
                ResultadoEnvio.GuardadoLocal(pinTemporal)
            }
        }
    }

    /** Sube todos los reportes pendientes (se dispara al recuperar conexión). */
    suspend fun sincronizarPendientes() {
        val cola = dao.pendientes()
        if (cola.isEmpty()) return
        if (!conectividad.hayConexion.value) return
        if (_sincronizando.value) return

        _sincronizando.value = true
        try {
            for (entidad in cola) {
                val respuesta = remoto.enviar(entidad.aBorrador(), conectividad.hayConexion.value)
                if (respuesta is ReporteRemoteDataSource.Respuesta.Aceptado) {
                    dao.marcarEnLinea(
                        entidad.id,
                        generarPinUnico(),
                        System.currentTimeMillis()
                    )
                } else {
                    // Sigue sin red: se deja para el próximo intento.
                    break
                }
            }
        } finally {
            _sincronizando.value = false
        }
    }

    private fun ReporteEntity.aBorrador() = BorradorReporte(
        fotoPath = fotoPath,
        categoria = categoria,
        tipo = tipo,
        carrera = carrera,
        aula = aula,
        esLaboratorio = esLaboratorio,
        nota = nota.ifBlank { null },
        destinoVigilancia = destinoVigilancia
    )

    /** PIN de 4 dígitos, único entre los reportes ya guardados. */
    private suspend fun generarPinUnico(): String {
        val existentes = dao.pinesExistentes().toSet()
        repeat(200) {
            val pin = (1000..9999).random().toString()
            if (pin !in existentes) return pin
        }
        return (1000..9999).random().toString()
    }

    /** Arranca la sincronización automática y la reintenta al volver la red. */
    fun iniciarSincronizacionAutomatica() {
        scope.launch {
            conectividad.hayConexion.collect { conectado ->
                if (conectado) sincronizarPendientes()
            }
        }
    }
}