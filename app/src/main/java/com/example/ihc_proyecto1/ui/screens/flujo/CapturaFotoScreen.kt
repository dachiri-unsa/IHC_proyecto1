package com.example.ihc_proyecto1.ui.screens.flujo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.data.local.FotoStore
import com.example.ihc_proyecto1.ui.components.BotonPrimario
import com.example.ihc_proyecto1.ui.components.BotonSecundario
import com.example.ihc_proyecto1.ui.theme.Guinda
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** PANTALLA 2 · REPORTAR OBJETO — Captura de la foto con la cámara. */
@Composable
fun PantallaCapturaFoto(
    onFotoCapturada: (String) -> Unit,
    onCancelar: () -> Unit
) {
    val contexto = LocalContext.current
    val ciclo = LocalLifecycleOwner.current
    val alcance = rememberCoroutineScope()

    var tienePermiso by remember { mutableStateOf(tienePermisoCamara(contexto)) }
    var permisoDenegado by remember { mutableStateOf(false) }
    var flash by remember { mutableStateOf(false) }
    var usarFrontal by remember { mutableStateOf(false) }
    var capturando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        tienePermiso = concedido
        permisoDenegado = !concedido
    }

    LaunchedEffect(Unit) {
        if (!tienePermiso) pedirPermiso.launch(Manifest.permission.CAMERA)
    }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val vistaPrevia = remember {
        PreviewView(contexto).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    var proveedor by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    LaunchedEffect(Unit) {
        proveedor = obtenerProveedorCamara(contexto)
    }

    LaunchedEffect(proveedor, usarFrontal, flash) {
        val actual = proveedor ?: return@LaunchedEffect
        runCatching {
            actual.unbindAll()
            val selector = if (usarFrontal) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = vistaPrevia.surfaceProvider
            }
            imageCapture.flashMode = if (flash) {
                ImageCapture.FLASH_MODE_ON
            } else {
                ImageCapture.FLASH_MODE_OFF
            }
            actual.bindToLifecycle(ciclo, selector, preview, imageCapture)
        }.onFailure {
            error = "No se pudo abrir la cámara en este dispositivo."
        }
    }

    fun tomarFoto() {
        if (capturando) return
        capturando = true
        error = null
        alcance.launch {
            val archivo = FotoStore.crearArchivo(contexto)
            val exito = capturar(imageCapture, archivo.absolutePath, contexto)
            capturando = false
            if (exito) onFotoCapturada(archivo.absolutePath) else error = "No se pudo tomar la foto."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (tienePermiso) {
            AndroidView(factory = { vistaPrevia }, modifier = Modifier.fillMaxSize())

            // Marco de encuadre.
            MarcoEncuadre(modifier = Modifier.fillMaxSize())
        }

        if (!tienePermiso) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_camara),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    text = if (permisoDenegado) {
                        "Necesitamos la cámara para fotografiar el objeto"
                    } else {
                        "Solicitando permiso de cámara…"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "La foto solo se usa para identificar el objeto encontrado en el reporte.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                if (permisoDenegado) {
                    Spacer(Modifier.height(22.dp))
                    BotonPrimario(
                        texto = "Dar permiso",
                        onClick = { pedirPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.width(220.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    BotonSecundario(
                        texto = "Cancelar",
                        onClick = onCancelar,
                        modifier = Modifier.width(220.dp)
                    )
                }
            }
        }

        // Barra superior.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoRedondo(onClick = onCancelar) {
                Icon(Icons.Filled.Close, "Cancelar", tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.weight(1f))
            if (tienePermiso) {
                IconoRedondo(onClick = { flash = !flash }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flash),
                        contentDescription = "Flash",
                        tint = if (flash) Color(0xFFFFD54F) else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                IconoRedondo(onClick = { usarFrontal = !usarFrontal }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cambiar_camara),
                        contentDescription = "Cambiar cámara",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Barra inferior con el disparador.
        if (tienePermiso) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Encuadra el objeto y toma la foto",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(Modifier.height(16.dp))
                Disparador(activo = !capturando, onClick = { tomarFoto() })
                if (error != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = error!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFFCDD2)
                    )
                }
            }
        }
    }
}

@Composable
private fun IconoRedondo(onClick: () -> Unit, contenido: @Composable () -> Unit) {
    Surface(
        color = Color.Black.copy(alpha = 0.4f),
        shape = CircleShape,
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) { contenido() }
    }
}

@Composable
private fun Disparador(activo: Boolean, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .border(4.dp, Color.White, CircleShape)
                .padding(7.dp)
                .clip(CircleShape)
                .clickable(enabled = activo, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (activo) 54.dp else 40.dp)
                    .background(if (activo) Guinda else Color(0xFF9A8A8A), CircleShape)
            )
        }
    }
}

/** Marco redondeado en el centro con el resto oscurecido. */
@Composable
private fun MarcoEncuadre(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.fillMaxSize()) { dibujarMarco() }
}

private fun DrawScope.dibujarMarco() {
    val grosor = 3.dp.toPx()
    val margen = 40.dp.toPx()
    val radio = 24.dp.toPx()
    val izquierda = margen
    val derecha = size.width - margen
    val arriba = (size.height * 0.20f)
    val abajo = (size.height * 0.78f)
    val scrim = Color.Black.copy(alpha = 0.42f)

    // Oscurece todo lo que está fuera del marco.
    drawRect(color = scrim, topLeft = Offset.Zero, size = Size(size.width, arriba))
    drawRect(
        color = scrim,
        topLeft = Offset(0f, abajo),
        size = Size(size.width, size.height - abajo)
    )
    drawRect(color = scrim, topLeft = Offset(0f, arriba), size = Size(izquierda, abajo - arriba))
    drawRect(
        color = scrim,
        topLeft = Offset(derecha, arriba),
        size = Size(size.width - derecha, abajo - arriba)
    )

    // Contorno del marco.
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(izquierda, arriba),
        size = Size(derecha - izquierda, abajo - arriba),
        cornerRadius = CornerRadius(radio),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = grosor)
    )
}

private suspend fun obtenerProveedorCamara(contexto: Context): ProcessCameraProvider? =
    suspendCancellableCoroutine { continuacion ->
        val futuro = ProcessCameraProvider.getInstance(contexto)
        futuro.addListener(
            {
                val proveedor = runCatching { futuro.get() }.getOrNull()
                if (continuacion.isActive) continuacion.resume(proveedor)
            },
            ContextCompat.getMainExecutor(contexto)
        )
    }

private suspend fun capturar(
    imageCapture: ImageCapture,
    ruta: String,
    contexto: Context
): Boolean = suspendCancellableCoroutine { continuacion ->
    val opciones = ImageCapture.OutputFileOptions.Builder(java.io.File(ruta)).build()
    imageCapture.takePicture(
        opciones,
        ContextCompat.getMainExecutor(contexto),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                if (continuacion.isActive) continuacion.resume(true)
            }

            override fun onError(exception: ImageCaptureException) {
                if (continuacion.isActive) continuacion.resume(false)
            }
        }
    )
}

private fun tienePermisoCamara(contexto: Context): Boolean =
    ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED