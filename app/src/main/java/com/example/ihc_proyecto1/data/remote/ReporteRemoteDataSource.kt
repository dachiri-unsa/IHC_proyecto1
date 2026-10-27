package com.example.ihc_proyecto1.data.remote

import com.example.ihc_proyecto1.data.model.BorradorReporte
import kotlinx.coroutines.delay

/**
 * "Servidor" simulado. Cuando exista un backend real, se reemplaza esta clase
 * por la llamada HTTP (Retrofit/Ktor) sin tocar el resto de la app.
 */
class ReporteRemoteDataSource {

    sealed interface Respuesta {
        data object Aceptado : Respuesta
        data object SinConexion : Respuesta

        /** El servidor rechazó el envío (distinto de no tener conexión). */
        data class Error(val mensaje: String) : Respuesta
    }

    /**
     * Simula el tiempo de subida de la foto y los datos.
     *
     * [falloForzado] es un interruptor de prueba para poder demostrar el caso
     * de error del servidor sin necesitar un backend real.
     */
    suspend fun enviar(
        borrador: BorradorReporte,
        hayConexion: Boolean,
        falloForzado: Boolean = false
    ): Respuesta {
        delay(DURACION_ENVIO_MS)
        return when {
            !hayConexion -> Respuesta.SinConexion
            falloForzado -> Respuesta.Error(MENSAJE_FALLO)
            else -> Respuesta.Aceptado
        }
    }

    companion object {
        const val DURACION_ENVIO_MS = 1800L
        const val MENSAJE_FALLO =
            "El servicio de reportes no está disponible. Intenta de nuevo en unos momentos."
    }
}