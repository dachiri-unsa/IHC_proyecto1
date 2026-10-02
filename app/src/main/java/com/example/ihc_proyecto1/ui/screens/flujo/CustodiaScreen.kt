package com.example.ihc_proyecto1.ui.screens.flujo

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.CajaPin
import com.example.ihc_proyecto1.ui.components.FilaAula
import com.example.ihc_proyecto1.ui.components.FilaDato
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Tarjeta
import com.example.ihc_proyecto1.ui.notificaciones.NotificadorCustodia
import com.example.ihc_proyecto1.ui.theme.ColorExito
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** PANTALLA 7 · CONFIRMACIÓN DE CUSTODIA. */
@Composable
fun PantallaCustodia(
    estado: BorradorUiState,
    onVolverAlInicio: () -> Unit
) {
    val contexto = LocalContext.current

    // Aviso del sistema al confirmar el deslinde. Solo una vez, aunque se rote la pantalla.
    var yaNotificado by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!yaNotificado) {
            yaNotificado = true
            NotificadorCustodia.mostrarDeslindeConfirmado(contexto, estado.destinoVigilancia)
        }
    }

    PantallaBase {
        Cabecera(
            titulo = "Confirmación de custodia",
            subtitulo = "Entrega registrada por vigilancia"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(18.dp))

            Surface(color = Color(0xFFE7F4E8), shape = CircleShape, modifier = Modifier.size(104.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_custodia),
                        contentDescription = null,
                        tint = ColorExito,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Custodia completada",
                style = MaterialTheme.typography.headlineSmall,
                color = TextoPrimario,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Vigilancia ya tiene registrado el objeto. ¡Gracias por colaborar!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(22.dp))

            CajaPin(pin = estado.pin.orEmpty(), temporal = !estado.enviadoOnline)

            Spacer(Modifier.height(18.dp))

            Tarjeta {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
                            Text("Entregado en", style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
                            Text(estado.destinoVigilancia, style = MaterialTheme.typography.titleMedium, color = Guinda)
                        }
                    }
                    FilaDato("Objeto", "${estado.categoria} · ${estado.tipo}")
                    FilaDato("Carrera", estado.carrera)
                    FilaAula(
                        etiqueta = "Aula / lugar",
                        aula = estado.aula,
                        esLaboratorio = estado.esLaboratorio
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            BotonPrimario(
                texto = "Volver al inicio",
                onClick = onVolverAlInicio,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}