package com.example.ihc_proyecto1.ui.screens.detalle

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.data.model.Reporte
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.CajaPin
import com.example.ihc_proyecto1.ui.components.ChipEstado
import com.example.ihc_proyecto1.ui.components.FilaAula
import com.example.ihc_proyecto1.ui.components.FilaDato
import com.example.ihc_proyecto1.ui.components.ImagenArchivo
import com.example.ihc_proyecto1.ui.components.MarcadorFoto
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Tarjeta
import com.example.ihc_proyecto1.ui.screens.misreportes.formatearFecha
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** Detalle de un reporte (desde "Mis reportes"). */
@Composable
fun PantallaDetalleReporte(
    reporte: Reporte?,
    onAtras: () -> Unit
) {
    PantallaBase {
        Cabecera(titulo = "Reporte", subtitulo = "Detalle del objeto encontrado", onAtras = onAtras)

        if (reporte == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Reporte no encontrado", style = MaterialTheme.typography.titleMedium)
            }
            return@PantallaBase
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ImagenArchivo(
                ruta = reporte.fotoPath,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop,
                esquinas = 18,
                marcador = { MarcadorFoto() }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipEstado(online = reporte.estado.name == "ONLINE")
                Text(
                    text = formatearFecha(reporte.creadoEn),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
            }

            CajaPin(pin = reporte.pin, temporal = reporte.pinTemporal)

            Tarjeta {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    FilaDato("Categoría", reporte.categoria)
                    FilaDato("Tipo", reporte.tipo)
                    FilaDato("Carrera", reporte.carrera)
                    FilaAula(
                        etiqueta = "Aula / lugar",
                        aula = reporte.aula,
                        esLaboratorio = reporte.esLaboratorio
                    )
                    if (!reporte.nota.isNullOrBlank()) FilaDato("Nota", reporte.nota)
                }
            }

            Tarjeta {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFF6EDED),
                            shape = CircleShape,
                            modifier = Modifier.size(38.dp).clip(CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_custodia),
                                    contentDescription = null,
                                    tint = Guinda,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(Modifier.size(10.dp))
                        Text(
                            text = "Llevar objeto a vigilancia",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = reporte.destinoVigilancia,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Guinda
                    )
                }
            }

            if (reporte.estado.name == "PENDIENTE") {
                Aviso(
                    texto = "Este reporte se guardó localmente con un PIN temporal. " +
                        "Se enviará solo cuando tengas conexión.",
                    icono = painterResource(R.drawable.ic_nube_offline),
                    color = ColorAviso,
                    fondo = Color(0xFFFBF0DC)
                )
            } else {
                Aviso(
                    texto = "Reporte sincronizado con el sistema de vigilancia.",
                    icono = painterResource(R.drawable.ic_check)
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}