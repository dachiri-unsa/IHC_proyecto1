package com.example.ihc_proyecto1.ui.screens.inicio

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.ui.components.Aviso
import com.example.ihc_proyecto1.ui.components.Cabecera
import com.example.ihc_proyecto1.ui.components.PantallaBase
import com.example.ihc_proyecto1.ui.components.Tarjeta
import com.example.ihc_proyecto1.ui.notificaciones.NotificadorCustodia
import com.example.ihc_proyecto1.ui.theme.ColorAviso
import com.example.ihc_proyecto1.ui.theme.Guinda
import com.example.ihc_proyecto1.ui.theme.TextoPrimario
import com.example.ihc_proyecto1.ui.theme.TextoSecundario

/** PANTALLA 1 · INICIO — Objetos perdidos (registro de hallazgo). */
@Composable
fun PantallaInicio(
    estado: InicioUiState,
    onReportar: () -> Unit,
    onMisReportes: () -> Unit,
    onCambiarModoSinConexion: (Boolean) -> Unit
) {
    var mostrarAjustes by remember { mutableStateOf(false) }

    val contexto = LocalContext.current

    // Permiso de notificaciones pedido acá, para no interrumpir al final del flujo.
    val pedirPermisoNotificaciones = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !NotificadorCustodia.tienePermiso(contexto)
        ) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    PantallaBase {
        Cabecera(
            titulo = "Objetos perdidos",
            subtitulo = "UNSA · Registro de objetos hallados",
            acciones = {
                Box {
                    Surface(
                        color = Color.White.copy(alpha = 0.16f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { mostrarAjustes = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Ajustes de prueba",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (estado.modoSinConexion) {
                Aviso(
                    texto = "Modo sin conexión activado: los reportes se guardarán localmente.",
                    icono = Icons.Filled.Settings
                )
            }

            if (estado.pendientes > 0) {
                Aviso(
                    texto = if (estado.hayConexion) {
                        "Sincronizando ${estado.pendientes} reporte(s) pendiente(s)…"
                    } else {
                        "${estado.pendientes} reporte(s) esperando conexión. Se enviarán automáticamente."
                    },
                    icono = Icons.Filled.Refresh,
                    color = ColorAviso,
                    fondo = Color(0xFFFBF0DC)
                )
            }

            TarjetaPrincipal(onClick = onReportar)

            Tarjeta(
                modifier = Modifier.clickable(onClick = onMisReportes)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mis reportes", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (estado.totalReportes == 0) {
                                "Todavía no has registrado objetos"
                            } else {
                                "${estado.totalReportes} reporte(s) · ${estado.pendientes} pendiente(s)"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                    Surface(
                        color = Color(0xFFF6EDED),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onMisReportes)
                    ) {
                        Text(
                            text = estado.totalReportes.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Guinda,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Tarjeta {
                Column {
                    Text("¿Cómo funciona?", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(10.dp))
                    PasoNumero(1, "Toma una foto clara del objeto encontrado.")
                    PasoNumero(2, "Completa la categoría, la carrera y el aula.")
                    PasoNumero(3, "Envía el reporte y recibe un PIN de 4 dígitos.")
                    PasoNumero(4, "Entrega el objeto en vigilancia con ese PIN.")
                }
            }
        }
    }

    if (mostrarAjustes) {
        DialogoAjustes(
            modoSinConexion = estado.modoSinConexion,
            hayConexionReal = estado.hayConexion,
            onCambiar = onCambiarModoSinConexion,
            onCerrar = { mostrarAjustes = false }
        )
    }
}

@Composable
private fun TarjetaPrincipal(onClick: () -> Unit) {
    Surface(
        color = Guinda,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .background(Color.White.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_camara),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Reportar objeto encontrado",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Toca para tomar la foto y generar tu reporte",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun PasoNumero(numero: Int, texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = Color(0xFFF6EDED), shape = CircleShape, modifier = Modifier.size(24.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = numero.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Guinda
                )
            }
        }
        Spacer(Modifier.size(10.dp))
        Text(text = texto, style = MaterialTheme.typography.bodyMedium, color = TextoPrimario)
    }
}

@Composable
private fun DialogoAjustes(
    modoSinConexion: Boolean,
    hayConexionReal: Boolean,
    onCambiar: (Boolean) -> Unit,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Ajustes de prueba") },
        text = {
            Column {
                Text(
                    text = if (hayConexionReal) {
                        "Este dispositivo tiene conexión a internet."
                    } else {
                        "Este dispositivo no tiene conexión a internet."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(14.dp))
                Surface(
                    color = Color(0xFFF6EDED),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFE6DDDD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Modo sin conexión", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Fuerza el caso offline para la demostración.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario
                            )
                        }
                        Switch(
                            checked = modoSinConexion,
                            onCheckedChange = onCambiar,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Guinda
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCerrar) { Text("Listo", color = Guinda) }
        }
    )
}