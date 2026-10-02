package com.example.ihc_proyecto1.ui.navigation

/** Rutas de la app. */
object Rutas {
    const val INICIO = "inicio"
    const val MIS_REPORTES = "mis_reportes"
    const val FLUJO = "flujo"

    const val ARG_ID = "id"
    const val DETALLE_REPORTE = "reporte/{$ARG_ID}"
    fun detalleReporte(id: String) = "reporte/$id"

    object Flujo {
        const val CAPTURAR = "flujo/capturar"
        const val REVISAR = "flujo/revisar"
        const val DATOS = "flujo/datos"
        const val RESUMEN = "flujo/resumen"
        const val ENVIANDO = "flujo/enviando"
        const val GENERADO = "flujo/generado"
        const val CUSTODIA = "flujo/custodia"
    }
}