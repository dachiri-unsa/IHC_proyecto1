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
    }

    /** Simula el tiempo de subida de la foto y los datos. */
    suspend fun enviar(borrador: BorradorReporte, hayConexion: Boolean): Respuesta {
        delay(DURACION_ENVIO_MS)
        return if (hayConexion) Respuesta.Aceptado else Respuesta.SinConexion
    }

    companion object {
        const val DURACION_ENVIO_MS = 1800L
    }
}