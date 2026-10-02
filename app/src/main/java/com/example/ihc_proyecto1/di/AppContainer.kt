package com.example.ihc_proyecto1.di

import android.content.Context
import com.example.ihc_proyecto1.data.local.ReporteDatabase
import com.example.ihc_proyecto1.data.network.ConectividadRepository
import com.example.ihc_proyecto1.data.remote.ReporteRemoteDataSource
import com.example.ihc_proyecto1.data.repository.ReporteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** Contenedor de dependencias simple (sin Hilt, la app es pequeña). */
class AppContainer(context: Context) {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val database = ReporteDatabase.obtener(context)

    val conectividad = ConectividadRepository(context, applicationScope)

    private val remoto = ReporteRemoteDataSource()

    val reporteRepository = ReporteRepository(
        dao = database.reporteDao(),
        remoto = remoto,
        conectividad = conectividad,
        scope = applicationScope
    ).also { it.iniciarSincronizacionAutomatica() }
}