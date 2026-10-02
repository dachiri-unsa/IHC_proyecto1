package com.example.ihc_proyecto1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ihc_proyecto1.data.model.EstadoReporte
import com.example.ihc_proyecto1.data.model.Reporte

@Entity(tableName = "reportes")
data class ReporteEntity(
    @PrimaryKey val id: String,
    val fotoPath: String,
    val categoria: String,
    val tipo: String,
    val carrera: String,
    val aula: String,
    val esLaboratorio: Boolean,
    val nota: String,
    val destinoVigilancia: String,
    val estado: String,
    val pin: String,
    val pinTemporal: Boolean,
    val creadoEn: Long,
    val sincronizadoEn: Long?
) {
    fun toDomain(): Reporte = Reporte(
        id = id,
        fotoPath = fotoPath,
        categoria = categoria,
        tipo = tipo,
        carrera = carrera,
        aula = aula,
        esLaboratorio = esLaboratorio,
        nota = nota.ifBlank { null },
        destinoVigilancia = destinoVigilancia,
        estado = runCatching { EstadoReporte.valueOf(estado) }.getOrDefault(EstadoReporte.PENDIENTE),
        pin = pin,
        pinTemporal = pinTemporal,
        creadoEn = creadoEn,
        sincronizadoEn = sincronizadoEn
    )
}