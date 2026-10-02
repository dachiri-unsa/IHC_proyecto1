package com.example.ihc_proyecto1.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.util.UUID

/**
 * Guarda las fotos en el almacenamiento interno de la app y las carga
 * decodificándolas con muestreo para no gastar tanta memoria.
 */
object FotoStore {

    private const val CARPETA = "fotos"

    fun crearArchivo(context: Context): File {
        val dir = File(context.filesDir, CARPETA).apply { mkdirs() }
        return File(dir, "IMG_${UUID.randomUUID()}.jpg")
    }

    fun eliminar(path: String?) {
        val ruta = path?.takeIf { it.isNotBlank() } ?: return
        runCatching { File(ruta).takeIf { it.exists() }?.delete() }
    }

    /** Decodifica la foto aplicándole la rotación de EXIF para que se vea vertical. */
    fun cargarBitmap(path: String?, anchoObjetivo: Int = 1080): Bitmap? {
        val ruta = path?.takeIf { it.isNotBlank() } ?: return null
        val archivo = File(ruta)
        if (!archivo.exists()) return null

        return runCatching {
            val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(ruta, limites)

            val opciones = BitmapFactory.Options().apply {
                inSampleSize = calcularMuestreo(limites.outWidth, limites.outHeight, anchoObjetivo)
            }
            val bitmap = BitmapFactory.decodeFile(ruta, opciones) ?: return null
            rotarSiHaceFalta(archivo, bitmap)
        }.getOrNull()
    }

    private fun calcularMuestreo(ancho: Int, alto: Int, objetivo: Int): Int {
        if (ancho <= 0 || alto <= 0 || objetivo <= 0) return 1
        var muestreo = 1
        while (ancho / (muestreo * 2) >= objetivo && alto / (muestreo * 2) >= objetivo) {
            muestreo *= 2
        }
        return muestreo
    }

    private fun rotarSiHaceFalta(archivo: File, bitmap: Bitmap): Bitmap {
        val orientacion = runCatching {
            ExifInterface(archivo).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

        val matriz = when (orientacion) {
            ExifInterface.ORIENTATION_ROTATE_90 -> Matrix().apply { postRotate(90f) }
            ExifInterface.ORIENTATION_ROTATE_180 -> Matrix().apply { postRotate(180f) }
            ExifInterface.ORIENTATION_ROTATE_270 -> Matrix().apply { postRotate(270f) }
            else -> null
        } ?: return bitmap

        return runCatching {
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matriz, true)
        }.getOrDefault(bitmap)
    }
}