package com.example.ihc_proyecto1.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Detecta si el dispositivo tiene conexión a internet de verdad.
 *
 * Se apoya en [ConnectivityManager] y en `NET_CAPABILITY_VALIDATED`, que el
 * sistema marca cuando la red realmente tiene salida a internet (no solo
 * estar conectado a un Wi-Fi sin señal).
 *
 * Además hay un interruptor de prueba ("Modo sin conexión") que permite forzar
 * el escenario offline aunque haya internet, para poder mostrar los dos casos
 * en una demostración.
 */
class ConectividadRepository(
    context: Context,
    scope: CoroutineScope
) {
    private val appContext = context.applicationContext

    private val _redDelSistema = MutableStateFlow(detectarRedDelSistema())
    private val _modoSinConexion = MutableStateFlow(false)
    private val _falloEnvioForzado = MutableStateFlow(false)

    /** Estado real de la red (sin el override de prueba). */
    val redDelSistema: StateFlow<Boolean> = _redDelSistema

    /** Interruptor de prueba del modo sin conexión. */
    val modoSinConexion: StateFlow<Boolean> = _modoSinConexion

    /** Interruptor de prueba para simular que el servidor rechaza el envío. */
    val falloEnvioForzado: StateFlow<Boolean> = _falloEnvioForzado

    /** Conexión efectiva que usa la app para decidir si envía o guarda local. */
    val hayConexion: StateFlow<Boolean> =
        combine(_redDelSistema, _modoSinConexion) { red, forzado -> red && !forzado }
            .stateIn(scope, SharingStarted.Eagerly, _redDelSistema.value && !_modoSinConexion.value)

    init {
        runCatching {
            val manager = appContext.getSystemService(ConnectivityManager::class.java)
                ?: return@runCatching
            manager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) = refrescar()
                override fun onLost(network: Network) = refrescar()
                override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) = refrescar()
            })
        }
    }

    fun actualizarModoSinConexion(activo: Boolean) {
        _modoSinConexion.value = activo
    }

    fun actualizarFalloEnvioForzado(activo: Boolean) {
        _falloEnvioForzado.value = activo
    }

    /** Se puede forzar la sincronización cuando se desactiva el modo prueba. */
    fun refrescar() {
        _redDelSistema.value = detectarRedDelSistema()
    }

    private fun detectarRedDelSistema(): Boolean {
        val manager = appContext.getSystemService(ConnectivityManager::class.java)
        if (manager == null) return false

        // Se recorren todas las redes porque getActiveNetwork() puede devolver
        // null si la app no tiene una red por defecto asignada.
        val hayRedConInternet = manager.allNetworks.any { red ->
            val capacidades = manager.getNetworkCapabilities(red) ?: return@any false
            capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return hayRedConInternet
    }
}