package com.example.ihc_proyecto1

import android.app.Application
import com.example.ihc_proyecto1.di.AppContainer
import com.example.ihc_proyecto1.ui.notificaciones.NotificadorCustodia

class ObjetosPerdidosApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificadorCustodia.crearCanal(this)
    }
}