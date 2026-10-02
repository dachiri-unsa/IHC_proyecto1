package com.example.ihc_proyecto1.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ihc_proyecto1.ObjetosPerdidosApp
import com.example.ihc_proyecto1.ui.screens.detalle.DetalleReporteViewModel
import com.example.ihc_proyecto1.ui.screens.flujo.ReportarViewModel
import com.example.ihc_proyecto1.ui.screens.inicio.InicioViewModel
import com.example.ihc_proyecto1.ui.screens.misreportes.MisReportesViewModel

/** DI manual: una factoría que arma los ViewModels con el contenedor de la app. */
object Factorias {

    private fun aplicacion(extras: androidx.lifecycle.viewmodel.CreationExtras): ObjetosPerdidosApp =
        extras[APPLICATION_KEY] as ObjetosPerdidosApp

    val General = viewModelFactory {
        initializer {
            ReportarViewModel(aplicacion(this).container.reporteRepository)
        }
        initializer {
            InicioViewModel(
                repositorio = aplicacion(this).container.reporteRepository,
                conectividad = aplicacion(this).container.conectividad
            )
        }
        initializer {
            MisReportesViewModel(aplicacion(this).container.reporteRepository)
        }
        initializer {
            DetalleReporteViewModel(
                repositorio = aplicacion(this).container.reporteRepository,
                id = createSavedStateHandle().get<String>(ARG_ID_REPORTE).orEmpty()
            )
        }
    }

    const val ARG_ID_REPORTE = "id"
}