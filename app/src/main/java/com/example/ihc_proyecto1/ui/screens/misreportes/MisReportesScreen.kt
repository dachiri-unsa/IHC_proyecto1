package com.example.ihc_proyecto1.ui.screens.misreportes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.data.model.Reporte
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.ChipEstado
import com.example.ihc_proyecto1.ui.components.ImagenArchivo
import com.example.ihc_proyecto1.ui.components.MarcadorFoto
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.PastillaLab
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** MIS REPORTES: lista con estado y PIN, con detalle al tocar. */
@Composable
fun PantallaMisReportes(
    reportes: List<Reporte>,
    onAtras: () -> Unit,
    onVerDetalle: (String) -> Unit
) {
    PantallaBase {
        Cabecera(
            titulo = "Mis reportes",
            subtitulo = "${reportes.size} reporte(s) registrado(s)",
            onAtras = onAtras
        )

        if (reportes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(color = Color(0xFFF6EDED), shape = CircleShape, modifier = Modifier.size(86.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_foto),
                            contentDescription = null,
                            tint = Guinda,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text("Aún no tienes reportes", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Cuando reportas un objeto encontrado aparecerá aquí con su PIN.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reportes, key = { it.id }) { reporte ->
                    TarjetaReporte(reporte = reporte, onClick = { onVerDetalle(reporte.id) })
                }
            }
        }
    }
}

@Composable
private fun TarjetaReporte(reporte: Reporte, onClick: () -> Unit) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Column {
            Row(modifier = Modifier.padding(14.dp)) {
                ImagenArchivo(
                    ruta = reporte.fotoPath,
                    modifier = Modifier.size(78.dp),
                    esquinas = 14,
                    marcador = { MarcadorFoto() }
                )
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reporte.tipo,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrimario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${reporte.categoria} · ${reporte.aula}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (reporte.esLaboratorio) {
                            Spacer(Modifier.width(6.dp))
                            PastillaLab()
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = formatearFecha(reporte.creadoEn),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextoSecundario
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (reporte.estado.name == "ONLINE") {
                                Color(0xFFFBEAEA)
                            } else {
                                Color(0xFFFBF0DC)
                            },
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "PIN ${reporte.pin}",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (reporte.estado.name == "ONLINE") Guinda else ColorAviso,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        ChipEstado(online = reporte.estado.name == "ONLINE")
                    }
                }
            }
        }
    }
}

internal fun formatearFecha(momento: Long): String =
    SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale("es", "PE")).format(Date(momento))