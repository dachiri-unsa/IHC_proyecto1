package com.example.ihc_proyecto1.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.ihc_proyecto1.data.model.AulaOpcion
import com.example.ihc_proyecto1.data.model.GRUPO_AULAS
import com.example.ihc_proyecto1.ui.theme.Borde
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.ColorError
import com.example.ihc_proyecto1.ui.theme.ColorExito
import com.example.ihc_proyecto1.ui.theme.FondoApp
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.GuindaContainer
import com.example.ihc_proyecto1.ui.theme.GuindaOnContainer
import com.example.ihc_proyecto1.ui.theme.Superficie
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** Raíz de casi todas las pantallas: fondo claro y contenido bajo las barras del sistema. */
@Composable
fun PantallaBase(
    modifier: Modifier = Modifier,
    fondo: Color = FondoApp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Surface(color = fondo, modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) { contenido() }
    }
}

/** Barra superior guinda con título, subtítulo, botón atrás y acciones. */
@Composable
fun Cabecera(
    titulo: String,
    subtitulo: String? = null,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Surface(color = Guinda, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (onAtras != null) 4.dp else 20.dp, end = 12.dp, top = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
            acciones()
        }
    }
}

@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    icono: ImageVector? = null
) {
    val colores = BotonColores(
        fondo = if (habilitado) Guinda else Color(0xFFD9C7C7),
        contenido = if (habilitado) Color.White else Color(0xFF8A7A7A)
    )
    Surface(
        color = colores.fondo,
        contentColor = colores.contenido,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = habilitado, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
            }
            Text(text = texto, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    icono: ImageVector? = null
) {
    Surface(
        color = Color.White,
        contentColor = if (habilitado) Guinda else Color(0xFFAFA0A0),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, if (habilitado) Guinda else Borde),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = habilitado, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
            }
            Text(text = texto, style = MaterialTheme.typography.labelLarge)
        }
    }
}

private data class BotonColores(val fondo: Color, val contenido: Color)

/** Fila etiqueta/valor usada en el resumen y en el detalle. */
@Composable
fun FilaDato(
    etiqueta: String,
    valor: String?,
    modifier: Modifier = Modifier,
    valorColor: Color = TextoPrimario
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = valor ?: "—",
            style = MaterialTheme.typography.bodyLarge,
            color = valorColor
        )
    }
}

/**
 * Icono flexible: acepta tanto un [ImageVector] (material icons) como un
 * [Painter] (vectores propios en res/drawable).
 */
@Composable
fun IconoFlexible(icono: Any?, modifier: Modifier = Modifier) {
    when (icono) {
        is ImageVector -> Icon(imageVector = icono, contentDescription = null, modifier = modifier)
        is androidx.compose.ui.graphics.painter.Painter ->
            Icon(painter = icono, contentDescription = null, modifier = modifier)

        else -> Unit
    }
}

/** Etiqueta de estado (En línea / Pendiente) y de tipo de PIN. */
@Composable
fun Chip(
    texto: String,
    colorTexto: Color,
    colorFondo: Color,
    modifier: Modifier = Modifier,
    icono: Any? = null
) {
    Surface(
        color = colorFondo,
        contentColor = colorTexto,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                IconoFlexible(icono, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(text = texto, style = MaterialTheme.typography.labelMedium, color = colorTexto)
        }
    }
}

@Composable
fun ChipEstado(online: Boolean, modifier: Modifier = Modifier) = if (online) {
    Chip("En línea", ColorExito, Color(0xFFE7F4E8), modifier)
} else {
    Chip("Pendiente", ColorAviso, Color(0xFFFBF0DC), modifier)
}

/** Banner informativo con icono. */
@Composable
fun Aviso(
    texto: String,
    modifier: Modifier = Modifier,
    icono: Any? = null,
    color: Color = GuindaOnContainer,
    fondo: Color = GuindaContainer
) {
    Surface(color = fondo, contentColor = color, shape = RoundedCornerShape(14.dp), modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                IconoFlexible(icono, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
            }
            Text(text = texto, style = MaterialTheme.typography.bodyMedium, color = color)
        }
    }
}

@Composable
fun AvisoError(texto: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFFFBE4E2),
        contentColor = ColorError,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = ColorError,
            modifier = Modifier.padding(12.dp)
        )
    }
}

/** Lista desplegable usada en el formulario (sin depender de APIs que cambian). */
@Composable
fun SelectorCampo(
    etiqueta: String,
    valor: String?,
    opciones: List<String>,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier,
    marcador: String = "Selecciona una opción",
    error: String? = null,
    habilitado: Boolean = true
) {
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = etiqueta, style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
        Spacer(Modifier.height(6.dp))
        Box {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, if (error != null) ColorError else Borde),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = habilitado) { expandido = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = valor ?: marcador,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (valor == null) TextoSecundario else TextoPrimario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (habilitado) Guinda else Color(0xFFBFB2B2)
                    )
                }
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = opcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextoPrimario
                            )
                        },
                        onClick = {
                            onSeleccion(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = ColorError,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/** Pastilla que marca un aula como aula de laboratorio. */
@Composable
fun PastillaLab(modifier: Modifier = Modifier) {
    Surface(
        color = GuindaContainer,
        contentColor = Guinda,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = "LAB",
            style = MaterialTheme.typography.labelMedium,
            color = Guinda,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

/** Nombre del aula con la pastilla LAB cuando corresponde. */
@Composable
fun ValorAula(
    nombre: String?,
    esLaboratorio: Boolean,
    modifier: Modifier = Modifier,
    vacio: String = "—"
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre?.takeIf { it.isNotBlank() } ?: vacio,
            style = MaterialTheme.typography.bodyLarge,
            color = if (nombre.isNullOrBlank()) TextoSecundario else TextoPrimario,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (esLaboratorio) {
            Spacer(Modifier.width(8.dp))
            PastillaLab()
        }
    }
}

/** Fila etiqueta/valor para el aula, con el indicador de laboratorio. */
@Composable
fun FilaAula(
    etiqueta: String,
    aula: String?,
    esLaboratorio: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(2.dp))
        ValorAula(nombre = aula, esLaboratorio = esLaboratorio)
    }
}

/**
 * Selector de aula: muestra primero las aulas de la carrera y después las del
 * único laboratorio de esa carrera, agrupadas y con la pastilla LAB. Al final
 * ofrece escribir el aula a mano por si no está en el catálogo.
 */
@Composable
fun SelectorAula(
    etiqueta: String,
    valor: String?,
    valorEsLaboratorio: Boolean,
    opciones: List<AulaOpcion>,
    onSeleccion: (AulaOpcion) -> Unit,
    modifier: Modifier = Modifier,
    marcador: String = "Selecciona el aula",
    error: String? = null,
    habilitado: Boolean = true,
    modoPersonalizado: Boolean = false,
    textoPersonalizado: String = "",
    onActivarPersonalizado: () -> Unit = {},
    onTextoPersonalizado: (String) -> Unit = {},
    errorPersonalizado: String? = null
) {
    var expandido by remember { mutableStateOf(false) }
    val grupos = remember(opciones) { opciones.groupBy { it.grupo }.toList() }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = etiqueta, style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
        Spacer(Modifier.height(6.dp))
        Box {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, if (error != null) ColorError else Borde),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = habilitado) { expandido = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ValorAula(
                        nombre = valor,
                        esLaboratorio = valorEsLaboratorio,
                        modifier = Modifier.weight(1f),
                        vacio = if (modoPersonalizado) "✏️ Aula escrita a mano…" else marcador
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (habilitado) Guinda else Color(0xFFBFB2B2)
                    )
                }
            }

            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                grupos.forEach { (grupo, items) ->
                    Text(
                        text = if (grupo == GRUPO_AULAS) "Aulas de la carrera" else grupo,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextoSecundario,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
                    )
                    items.forEach { opcion ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = opcion.nombre,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextoPrimario,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (opcion.esLaboratorio) {
                                        Spacer(Modifier.width(8.dp))
                                        PastillaLab()
                                    }
                                }
                            },
                            onClick = {
                                onSeleccion(opcion)
                                expandido = false
                            }
                        )
                    }
                    HorizontalDivider(color = Color(0xFFF0E3E3))
                }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Escribir otra aula…",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Guinda
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Guinda
                        )
                    },
                    onClick = {
                        onActivarPersonalizado()
                        expandido = false
                    }
                )
            }
        }

        if (modoPersonalizado) {
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = textoPersonalizado,
                onValueChange = onTextoPersonalizado,
                label = { Text("Nombre del aula") },
                placeholder = { Text("Ej.: 512-B (Pabellón B)") },
                singleLine = true,
                isError = errorPersonalizado != null,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Guinda,
                    unfocusedBorderColor = Borde,
                    focusedLabelColor = Guinda,
                    cursorColor = Guinda,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (errorPersonalizado != null) {
                Text(
                    text = errorPersonalizado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ColorError,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = ColorError,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/** Barra de pasos del flujo: Foto → Datos → Envío → Listo */
@Composable
fun Stepper(etiquetas: List<String>, indiceActual: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        etiquetas.forEachIndexed { indice, etiqueta ->
            val activo = indice <= indiceActual
            Box(
                modifier = Modifier
                    .size(if (indice == indiceActual) 12.dp else 9.dp)
                    .background(
                        color = if (activo) Guinda else Color(0xFFDCCFCF),
                        shape = CircleShape
                    )
            )
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelMedium,
                color = if (indice == indiceActual) Guinda else TextoSecundario,
                maxLines = 1
            )
            if (indice != etiquetas.lastIndex) {
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(
                            color = if (indice < indiceActual) Guinda else Color(0xFFE7DCDC)
                        )
                )
                Spacer(Modifier.width(6.dp))
            }
        }
    }
}

/** Caja del PIN de 4 dígitos. */
@Composable
fun CajaPin(
    pin: String,
    temporal: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (temporal) Color(0xFFFBF0DC) else GuindaContainer,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, if (temporal) Color(0xFFEBD5A8) else Guinda),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (temporal) "PIN TEMPORAL" else "PIN DEL REPORTE",
                style = MaterialTheme.typography.labelMedium,
                color = if (temporal) ColorAviso else Guinda
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = pin,
                style = MaterialTheme.typography.displaySmall,
                color = if (temporal) ColorAviso else Guinda
            )
        }
    }
}

/** Tarjeta blanca con borde suave. */
@Composable
fun Tarjeta(
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = Superficie,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Borde),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) { contenido() }
    }
}