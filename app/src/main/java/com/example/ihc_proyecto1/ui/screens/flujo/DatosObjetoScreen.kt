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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.data.model.AulaOpcion
import com.example.ihc_proyecto1.data.model.Catalogos
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.ImagenArchivo
import com.example.ihc_proyecto1.ui.components.MarcadorFoto
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.SelectorAula
import com.example.ihc_proyecto1.ui.components.SelectorCampo
import com.example.ihc_proyecto1.ui.components.Stepper
import com.example.ihc_proyecto1.ui.theme.Borde
import com.example.ihc_proyecto1.ui.theme.ColorError
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoSecundario
import kotlinx.coroutines.launch

/** PANTALLA 4 · DATOS DEL OBJETO — Formulario. */
@Composable
fun PantallaDatosObjeto(
    estado: BorradorUiState,
    onCategoria: (String) -> Unit,
    onTipo: (String) -> Unit,
    onCarrera: (String) -> Unit,
    onAula: (AulaOpcion) -> Unit,
    onEscribirAula: () -> Unit,
    onTextoAula: (String) -> Unit,
    onNota: (String) -> Unit,
    onCambiarFoto: () -> Unit,
    onTomarFoto: () -> Unit,
    onContinuar: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val alcance = rememberCoroutineScope()

    PantallaBase {
        Cabecera(
            titulo = "Datos del objeto",
            subtitulo = "Paso 2 de 2 · Completa la información",
            onAtras = if (estado.fotoPath == null) onTomarFoto else onCambiarFoto
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Stepper(listOf("Foto", "Datos", "Envío"), indiceActual = 1)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ImagenArchivo(
                        ruta = estado.fotoPath,
                        modifier = Modifier.size(84.dp),
                        esquinas = 14,
                        marcador = { MarcadorFoto() }
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fotografía del objeto", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Toca la imagen para cambiarla",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                    TextButton(
                        onClick = if (estado.fotoPath == null) onTomarFoto else onCambiarFoto
                    ) {
                        Text("Editar", color = Guinda)
                    }
                }

                estado.errorDe("Foto")?.let { errorFoto ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = errorFoto,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorError,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onTomarFoto) {
                            Text("Tomar foto", color = Guinda)
                        }
                    }
                }

                SelectorCampo(
                    etiqueta = "Categoría",
                    valor = estado.categoria,
                    opciones = Catalogos.categorias,
                    onSeleccion = onCategoria,
                    error = estado.errorDe("Categoría")
                )

                SelectorCampo(
                    etiqueta = "Tipo",
                    valor = estado.tipo,
                    opciones = estado.tiposDisponibles,
                    onSeleccion = onTipo,
                    marcador = if (estado.categoria == null) {
                        "Elige primero una categoría"
                    } else {
                        "Selecciona el tipo"
                    },
                    error = estado.errorDe("Tipo"),
                    habilitado = estado.categoria != null
                )

                SelectorCampo(
                    etiqueta = "Carrera",
                    valor = estado.carrera,
                    opciones = Catalogos.carreras,
                    onSeleccion = onCarrera,
                    marcador = "Selecciona la carrera",
                    error = estado.errorDe("Carrera")
                )

                SelectorAula(
                    etiqueta = "Aula / lugar",
                    valor = estado.aula,
                    valorEsLaboratorio = estado.esLaboratorio,
                    opciones = estado.aulasDisponibles,
                    onSeleccion = onAula,
                    marcador = if (estado.carrera == null) {
                        "Elige primero la carrera"
                    } else {
                        "Selecciona el aula donde lo encontraste"
                    },
                    error = if (estado.aulaPersonalizada) null else estado.errorDe("Aula"),
                    habilitado = estado.carrera != null,
                    modoPersonalizado = estado.aulaPersonalizada,
                    textoPersonalizado = estado.aula.orEmpty(),
                    onActivarPersonalizado = onEscribirAula,
                    onTextoPersonalizado = onTextoAula,
                    errorPersonalizado = if (estado.aulaPersonalizada) {
                        estado.errorDe("Aula")
                    } else {
                        null
                    }
                )

                if (estado.esLaboratorio && estado.laboratorioDeLaCarrera != null) {
                    Aviso(
                        texto = "Esta aula es del ${estado.laboratorioDeLaCarrera} de la carrera elegida.",
                        icono = painterResource(R.drawable.ic_custodia)
                    )
                }

                OutlinedTextField(
                    value = estado.nota,
                    onValueChange = onNota,
                    label = { Text("Nota (opcional)") },
                    placeholder = { Text("Ej.: estaba debajo de la mesa, en el pasillo…") },
                    minLines = 3,
                    isError = estado.notaExcedeLimite,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Guinda,
                        unfocusedBorderColor = Borde,
                        focusedLabelColor = Guinda,
                        cursorColor = Guinda,
                        errorBorderColor = ColorError,
                        errorLabelColor = ColorError,
                        errorCursorColor = ColorError,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    supportingText = {
                        Column {
                            Text(
                                text = "${estado.nota.length}/$LIMITE_NOTA",
                                color = if (estado.notaExcedeLimite) ColorError else TextoSecundario
                            )
                            estado.errorDe("Nota")?.let { errorNota ->
                                Text(text = errorNota, color = ColorError)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (estado.carrera != null) {
                    Aviso(
                        texto = "Destino de entrega: ${estado.destinoVigilancia}",
                        icono = painterResource(R.drawable.ic_custodia)
                    )
                }

                BotonPrimario(
                    texto = "Continuar…",
                    onClick = {
                        estado.primerProblema()?.let { problema ->
                            alcance.launch { snackbarHostState.showSnackbar(problema) }
                        }
                        onContinuar()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}
