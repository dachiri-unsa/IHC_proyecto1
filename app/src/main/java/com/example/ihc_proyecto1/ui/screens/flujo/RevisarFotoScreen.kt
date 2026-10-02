package com.example.ihc_proyecto1.ui.screens.flujo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.BotonSecundario
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.ImagenArchivo
import com.example.ihc_proyecto1.ui.components.MarcadorFoto
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Stepper
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/**
 * PANTALLA 3 · REVISAR FOTOGRAFÍA — La validación la hace el usuario:
 * si la foto no sirve, repite; si sirve, continúa.
 */
@Composable
fun PantallaRevisarFoto(
    rutaFoto: String?,
    onRepetir: () -> Unit,
    onContinuar: () -> Unit
) {
    PantallaBase {
        Cabecera(
            titulo = "Revisar fotografía",
            subtitulo = "Paso 1 de 2 · Validación de la foto",
            onAtras = onRepetir
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Stepper(listOf("Foto", "Datos", "Envío"), indiceActual = 0)

            ImagenArchivo(
                ruta = rutaFoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Fit,
                esquinas = 20,
                marcador = { MarcadorFoto(texto = "Sin foto") }
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿La fotografía es válida?",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextoPrimario
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Revisa que se vea el objeto con claridad, sin manchas ni reflejos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            }

            Aviso(
                texto = "Si tomas una foto repetida, se descarta la anterior.",
                icono = painterResource(R.drawable.ic_foto)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonSecundario(
                    texto = "Repetir",
                    onClick = onRepetir,
                    icono = Icons.Filled.Refresh,
                    modifier = Modifier.weight(1f)
                )
                BotonPrimario(
                    texto = "Continuar…",
                    onClick = onContinuar,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}