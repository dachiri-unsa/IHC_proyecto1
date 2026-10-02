package com.example.ihc_proyecto1.ui.screens.flujo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.BotonSecundario
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.CajaPin
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Tarjeta
import com.example.ihc_proyecto1.ui.theme.Borde
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.ColorExito
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

/** PANTALLA 6 · REPORTE GENERADO — Variante online u offline. */
@Composable
fun PantallaReporteGenerado(
    estado: BorradorUiState,
    onIrACustodia: () -> Unit
) {
    val pin = estado.pin.orEmpty()
    val online = estado.enviadoOnline

    BackHandler(enabled = true) { }

    PantallaBase {
        Cabecera(
            titulo = if (online) "Reporte generado" else "Almacenado localmente",
            subtitulo = if (online) "Registro completado" else "Sin conexión a internet"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            Surface(
                color = if (online) Color(0xFFE7F4E8) else Color(0xFFFBF0DC),
                shape = CircleShape,
                modifier = Modifier.size(104.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            if (online) R.drawable.ic_check else R.drawable.ic_nube_offline
                        ),
                        contentDescription = null,
                        tint = if (online) ColorExito else ColorAviso,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = if (online) "¡Reporte registrado!" else "Almacenado localmente",
                style = MaterialTheme.typography.headlineSmall,
                color = TextoPrimario,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (online) {
                    "Muestra este PIN en vigilancia para entregar el objeto."
                } else {
                    "Tu reporte se enviará solo cuando tengas conexión. Guarda este PIN temporal."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(22.dp))

            CajaPin(pin = pin, temporal = !online)

            Spacer(Modifier.height(18.dp))

            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFF6EDED), shape = CircleShape, modifier = Modifier.size(38.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.ic_custodia),
                                contentDescription = null,
                                tint = Guinda,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Llevar objeto a vigilancia", style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
                        Text(
                            text = estado.destinoVigilancia,
                            style = MaterialTheme.typography.titleMedium,
                            color = Guinda
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            EsperaSecretaria(onContinuar = onIrACustodia)

            Spacer(Modifier.height(8.dp))
        }
    }
}

/**
 * Simula la confirmación de la secretaría: por ahora solo espera 10 segundos y
 * pasa sola a la pantalla de custodia. Cuando exista la API, esto se reemplaza
 * por la confirmación real del servidor.
 */
@Composable
private fun EsperaSecretaria(onContinuar: () -> Unit) {
    var restantes by remember { mutableIntStateOf(ESPERA_SEGUNDOS) }

    LaunchedEffect(Unit) {
        while (restantes > 0) {
            delay(1000)
            restantes -= 1
        }
        onContinuar()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Borde),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_espera),
                    contentDescription = null,
                    tint = Guinda,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Esperando confirmación de entrega",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrimario
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Se espera la confirmación de la secretaría. Continuando en ${restantes}s…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(
                    color = Guinda,
                    trackColor = Color(0xFFF0E3E3),
                    progress = {
                        (ESPERA_SEGUNDOS - restantes + 1).toFloat() / ESPERA_SEGUNDOS
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                Spacer(Modifier.height(12.dp))
                BotonSecundario(
                    texto = "Continuar ahora",
                    onClick = onContinuar,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private const val ESPERA_SEGUNDOS = 10