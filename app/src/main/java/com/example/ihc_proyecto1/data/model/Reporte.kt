package com.example.ihc_proyecto1.data.model

/** Estado de un reporte: pendiente de red (offline) o ya subido (online). */
enum class EstadoReporte {
    PENDIENTE,
    ONLINE;

    val etiqueta: String
        get() = when (this) {
            PENDIENTE -> "Pendiente"
            ONLINE -> "En línea"
        }

    val descripcion: String
        get() = when (this) {
            PENDIENTE -> "Almacenado localmente, se enviará solo cuando tengas conexión"
            ONLINE -> "Reporte registrado en el sistema de vigilancia"
        }
}

/** Reporte ya guardado en la base local. */
data class Reporte(
    val id: String,
    val fotoPath: String,
    val categoria: String,
    val tipo: String,
    val carrera: String,
    val aula: String,
    /** El aula elegida pertenece al laboratorio de la carrera. */
    val esLaboratorio: Boolean,
    val nota: String?,
    val destinoVigilancia: String,
    val estado: EstadoReporte,
    val pin: String,
    val pinTemporal: Boolean,
    val creadoEn: Long,
    val sincronizadoEn: Long?
)

/** Datos que el usuario está llenando en el formulario, antes de enviar. */
data class BorradorReporte(
    val fotoPath: String,
    val categoria: String,
    val tipo: String,
    val carrera: String,
    val aula: String,
    val esLaboratorio: Boolean,
    val nota: String?,
    val destinoVigilancia: String
)

/** Resultado del intento de envío (simulado). */
sealed interface ResultadoEnvio {
    data class Enviado(val pin: String) : ResultadoEnvio
    data class GuardadoLocal(val pin: String) : ResultadoEnvio

    /** El servidor falló; el reporte queda guardado localmente como pendiente. */
    data class Fallido(
        val id: String,
        val pin: String,
        val mensaje: String
    ) : ResultadoEnvio
}