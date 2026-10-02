package com.example.ihc_proyecto1.ui.screens.misreportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihc_proyecto1.data.model.Reporte
import com.example.ihc_proyecto1.data.repository.ReporteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisReportesViewModel(
    private val repositorio: ReporteRepository
) : ViewModel() {

    val reportes: StateFlow<List<Reporte>> = repositorio.reportes
        .map { lista -> lista }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val sincronizando: StateFlow<Boolean> = repositorio.sincronizando

    fun reintentar() {
        viewModelScope.launch { repositorio.sincronizarPendientes() }
    }
}