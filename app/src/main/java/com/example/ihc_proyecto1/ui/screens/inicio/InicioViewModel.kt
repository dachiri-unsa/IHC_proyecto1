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
    val sincronizando: Boolean = false
)

class InicioViewModel(
    private val repositorio: ReporteRepository,
    private val conectividad: ConectividadRepository
) : ViewModel() {

    val estado: StateFlow<InicioUiState> = combine(
        repositorio.reportes,
        repositorio.pendientes,
        conectividad.hayConexion,
        conectividad.modoSinConexion,
        repositorio.sincronizando
    ) { reportes, pendientes, hayConexion, modoSinConexion, sincronizando ->
        InicioUiState(
            totalReportes = reportes.size,
            pendientes = pendientes,
            hayConexion = hayConexion,
            modoSinConexion = modoSinConexion,
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
}