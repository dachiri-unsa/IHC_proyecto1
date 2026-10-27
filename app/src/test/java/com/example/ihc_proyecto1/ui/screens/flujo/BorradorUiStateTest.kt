package com.example.ihc_proyecto1.ui.screens.flujo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BorradorUiStateTest {

    private fun estadoValido(
        nota: String = "",
        fotoPath: String? = "/tmp/foto.jpg"
    ) = BorradorUiState(
        fotoPath = fotoPath,
        categoria = "Tecnología",
        tipo = "Laptop",
        carrera = "Ingeniería de Sistemas",
        aula = "Aula 101",
        nota = nota
    )

    @Test
    fun `la nota de 200 caracteres es valida`() {
        val estado = estadoValido(nota = "a".repeat(LIMITE_NOTA))

        assertTrue(estado.datosCompletos)
        assertNull(estado.errorDe("Nota"))
        assertNotNull(estado.aBorrador())
    }

    @Test
    fun `la nota de 201 caracteres muestra error y bloquea el envio`() {
        val estado = estadoValido(nota = "a".repeat(LIMITE_NOTA + 1))

        assertFalse(estado.datosCompletos)
        assertEquals(
            "La nota no puede superar los 200 caracteres (201/200).",
            estado.errorDe("Nota")
        )
        assertNull(estado.aBorrador())
        assertEquals("La nota supera los 200 caracteres.", estado.primerProblema())
    }

    @Test
    fun `sin foto se muestra el error de foto obligatoria`() {
        val estado = estadoValido(fotoPath = null)

        assertFalse(estado.datosCompletos)
        assertEquals(
            "La foto es obligatoria. Toma una foto del objeto para continuar.",
            estado.errorDe("Foto")
        )
        assertNull(estado.aBorrador())
        assertEquals("Falta la foto del objeto.", estado.primerProblema())
    }

    @Test
    fun `los datos completos requieren categoria tipo carrera y aula`() {
        val estado = estadoValido().copy(categoria = null)

        assertFalse(estado.datosCompletos)
        assertNull(estado.aBorrador())
    }
}
