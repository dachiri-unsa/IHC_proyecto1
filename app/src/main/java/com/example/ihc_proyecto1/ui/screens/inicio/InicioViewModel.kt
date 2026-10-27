package com.example.ihc_proyecto1.ui.screens.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihc_proyecto1.data.network.ConectividadRepository
import com.example.ihc_proyecto1.data.repository.ReporteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InicioUiState(
    val totalReportes: Int = 0,
    val pendientes: Int = 0,
    val hayConexion: Boolean = true,
    val modoSinConexion: Boolean = false,
    val simularFalloEnvio: Boolean = false,
    val sincronizando: Boolean = false
)

class InicioViewModel(
    private val repositorio: ReporteRepository,
    private val conectividad: ConectividadRepository
) : ViewModel() {

    private val resumenReportes = combine(
        repositorio.reportes,
        repositorio.pendientes,
        repositorio.sincronizando
    ) { reportes, pendientes, sincronizando ->
        Triple(reportes.size, pendientes, sincronizando)
    }

    val estado: StateFlow<InicioUiState> = combine(
        resumenReportes,
        conectividad.hayConexion,
        conectividad.modoSinConexion,
        conectividad.falloEnvioForzado
    ) { resumen, hayConexion, modoSinConexion, simularFalloEnvio ->
        val (totalReportes, pendientes, sincronizando) = resumen
        InicioUiState(
            totalReportes = totalReportes,
            pendientes = pendientes,
            hayConexion = hayConexion,
            modoSinConexion = modoSinConexion,
            simularFalloEnvio = simularFalloEnvio,
            sincronizando = sincronizando
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InicioUiState())

    /** Interruptor de prueba para demostrar el caso sin conexión. */
    fun cambiarModoSinConexion(activo: Boolean) {
        conectividad.actualizarModoSinConexion(activo)
        if (!activo) {
            // Al desactivar el modo prueba, se intenta subir lo pendiente.
            viewModelScope.launch { repositorio.sincronizarPendientes() }
        }
    }

    fun reintentarSincronizacion() {
        viewModelScope.launch { repositorio.sincronizarPendientes() }
    }

    /** Interruptor de prueba para demostrar el error del servidor al enviar. */
    fun cambiarSimulacionFallo(activo: Boolean) {
        conectividad.actualizarFalloEnvioForzado(activo)
        if (!activo) {
            // Al desactivar el fallo simulado, se reintenta subir lo pendiente.
            viewModelScope.launch { repositorio.sincronizarPendientes() }
        }
    }
}