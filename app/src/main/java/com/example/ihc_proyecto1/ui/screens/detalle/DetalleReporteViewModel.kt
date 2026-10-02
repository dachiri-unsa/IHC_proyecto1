package com.example.ihc_proyecto1.ui.screens.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihc_proyecto1.data.model.Reporte
import com.example.ihc_proyecto1.data.repository.ReporteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetalleReporteViewModel(
    repositorio: ReporteRepository,
    id: String
) : ViewModel() {

    val reporte: StateFlow<Reporte?> = repositorio.reporte(id)
        .map { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}