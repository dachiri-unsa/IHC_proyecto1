package com.example.ihc_proyecto1.ui.screens.flujo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.AvisoError
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.BotonSecundario
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.theme.ColorError
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** PANTALLA 5.2 · ENVIANDO REPORTE — No se puede salir mientras sube. */
@Composable
fun PantallaEnviando(
    hayConexion: Boolean,
    errorEnvio: String?,
    hayReporteGuardado: Boolean,
    onEnviar: () -> Unit,
    onReintentar: () -> Unit,
    onContinuarGuardado: () -> Unit
) {
    // Mientras envía no se permite volver atrás ni cerrar el flujo.
    BackHandler(enabled = true) { }

    // Solo se dispara el envío inicial; los reintentos los pide el usuario.
    LaunchedEffect(Unit) {
        if (errorEnvio == null) onEnviar()
    }

    PantallaBase {
        if (errorEnvio == null) {
            ContenidoEnviando(hayConexion = hayConexion)
        } else {
            ContenidoErrorEnvio(
                mensaje = errorEnvio,
                hayReporteGuardado = hayReporteGuardado,
                onReintentar = onReintentar,
                onContinuarGuardado = onContinuarGuardado
            )
        }
    }
}

@Composable
private fun ContenidoEnviando(hayConexion: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = Guinda.copy(alpha = 0.08f),
            shape = CircleShape,
            modifier = Modifier.size(140.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = Guinda,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(108.dp)
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Enviando reporte",
            style = MaterialTheme.typography.headlineSmall,
            color = TextoPrimario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = if (hayConexion) {
                "Subiendo la foto y los datos del objeto…"
            } else {
                "No detectamos conexión. El reporte se guardará localmente con un PIN temporal."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(30.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_pin),
                contentDescription = null,
                tint = Guinda,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Por favor, no cierres la aplicación",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
    }
}

/** Variante cuando el servidor rechaza el envío (no es un problema de conexión). */
@Composable
private fun ContenidoErrorEnvio(
    mensaje: String,
    hayReporteGuardado: Boolean,
    onReintentar: () -> Unit,
    onContinuarGuardado: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = ColorError.copy(alpha = 0.10f),
            shape = CircleShape,
            modifier = Modifier.size(132.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = null,
                    tint = ColorError,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = "No pudimos enviar el reporte",
            style = MaterialTheme.typography.headlineSmall,
            color = TextoPrimario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Tu reporte quedó guardado en este dispositivo como pendiente.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(18.dp))

        AvisoError(texto = mensaje)

        Spacer(Modifier.height(26.dp))

        BotonPrimario(
            texto = "Reintentar envío",
            onClick = onReintentar,
            modifier = Modifier.fillMaxWidth()
        )

        if (hayReporteGuardado) {
            Spacer(Modifier.height(10.dp))

            BotonSecundario(
                texto = "Ver reporte guardado",
                onClick = onContinuarGuardado,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
