package com.example.ihc_proyecto1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ihc_proyecto1.R
import com.example.ihc_proyecto1.data.local.FotoStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Muestra una foto guardada en disco. La decodifica fuera del hilo principal y
 * con muestreo para no consumir tanta memoria.
 */
@Composable
fun ImagenArchivo(
    ruta: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    descripcion: String? = "Fotografía del objeto",
    esquinas: Int = 18,
    marcador: @Composable BoxScope.() -> Unit = {}
) {
    var imagen by remember(ruta) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(ruta) {
        imagen = withContext(Dispatchers.IO) { FotoStore.cargarBitmap(ruta)?.asImageBitmap() }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(esquinas.dp))
            .background(Color(0xFFEFE7E7)),
        contentAlignment = Alignment.Center
    ) {
        val actual = imagen
        if (actual != null) {
            Image(
                bitmap = actual,
                contentDescription = descripcion,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            marcador()
        }
    }
}

@Composable
fun MarcadorFoto(modifier: Modifier = Modifier, texto: String = "Sin foto") {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_foto),
            contentDescription = null,
            tint = Color(0xFFBCAEAE),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFFBCAEAE),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}