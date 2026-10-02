package com.example.ihc_proyecto1

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ihc_proyecto1.ui.navigation.NavegacionApp
import com.example.ihc_proyecto1.ui.theme.IHC_proyecto1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fondo claro con iconos oscuros: toda la app usa fondo casi blanco.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            IHC_proyecto1Theme {
                NavegacionApp()
            }
        }
    }
}
