package com.example.ihc_proyecto1.ui.screens.flujo

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.FilaAula
import com.example.ihc_proyecto1.ui.components.FilaDato
import com.example.ihc_proyecto1.ui.components.ImagenArchivo
import com.example.ihc_proyecto1.ui.components.MarcadorFoto
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Stepper
import com.example.ihc_proyecto1.ui.components.Tarjeta
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** PANTALLA 5.1 · REVISAR Y ENVIAR — Resumen con edición. */
@Composable
fun PantallaResumen(
    estado: BorradorUiState,
    onEditarFoto: () -> Unit,
    onEditarDatos: () -> Unit,
    onEnviar: () -> Unit
) {
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    PantallaBase {
        Cabecera(
            titulo = "Revisar y enviar",
            subtitulo = "Paso 3 de 3 · Revisa antes de enviar",
            onAtras = onEditarDatos
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Stepper(listOf("Foto", "Datos", "Envío"), indiceActual = 2)

            Tarjeta {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Fotografía",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onEditarFoto) {
                            Text("Editar", color = Guinda)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = onEditarFoto)
                    ) {
                        ImagenArchivo(
                            ruta = estado.fotoPath,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            esquinas = 16,
                            marcador = { MarcadorFoto() }
                        )
                    }
                }
            }

            Tarjeta {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Información del objeto",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onEditarDatos) {
                            Text("Editar", color = Guinda)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FilaDato("Categoría", estado.categoria)
                        FilaDato("Tipo", estado.tipo)
                        FilaDato("Carrera", estado.carrera)
                        FilaAula(
                            etiqueta = "Aula / lugar",
                            aula = estado.aula,
                            esLaboratorio = estado.esLaboratorio
                        )
                        if (estado.nota.isNotBlank()) FilaDato("Nota", estado.nota)
                    }
                }
            }

            Aviso(
                texto = "Entrega el objeto en ${estado.destinoVigilancia} indicando el PIN.",
                icono = painterResource(R.drawable.ic_custodia)
            )

            if (!estado.hayConexion) {
                Aviso(
                    texto = "No hay conexión: el reporte se guardará localmente con un PIN temporal.",
                    icono = painterResource(R.drawable.ic_nube_offline),
                    color = ColorAviso,
                    fondo = Color(0xFFFBF0DC)
                )
            }

            BotonPrimario(
                texto = "Enviar reporte",
                onClick = { mostrarConfirmacion = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("¿Enviar reporte?") },
            text = {
                Column {
                    Text(
                        text = "Se enviará el reporte de ${estado.categoria} · ${estado.tipo} " +
                            "encontrado en ${estado.aula}."
                    )
                    if (!estado.hayConexion) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "No hay conexión: se guardará localmente con un PIN temporal.",
                            color = ColorAviso
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        onEnviar()
                    }
                ) {
                    Text("Enviar", color = Guinda)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}