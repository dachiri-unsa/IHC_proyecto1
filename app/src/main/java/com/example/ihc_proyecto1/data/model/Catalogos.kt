package com.example.ihc_proyecto1.data.model

/**
 * Un único laboratorio por carrera. El laboratorio tiene sus propias aulas
 * (numeración baja del mismo pabellón: 101-A, 102-A, …).
 */
data class Laboratorio(val nombre: String, val aulas: List<String>)

/** Aulas de la carrera + su laboratorio. */
data class CatalogoCarrera(val aulas: List<String>, val laboratorio: Laboratorio?)

/** Opción que se muestra en el desplegable de aula. */
data class AulaOpcion(
    val nombre: String,
    val esLaboratorio: Boolean,
    val grupo: String
)

/** Grupo del catálogo que agrupa las aulas que no son de laboratorio. */
const val GRUPO_AULAS = "Aulas"

/**
 * Catálogos de ejemplo. Se pueden cambiar libremente: son solo listas de
 * strings en el código.
 */
object Catalogos {

    val categorias: List<String> = listOf(
        "Celular / Tablet",
        "Documento / Paper",
        "Llaves",
        "Tarjeta / Billete",
        "Ropa",
        "Libro / Cuaderno",
        "Accesorio",
        "Electrodoméstico",
        "Joyería",
        "Dinero",
        "Otro"
    )

    val tiposPorCategoria: Map<String, List<String>> = linkedMapOf(
        "Celular / Tablet" to listOf(
            "Smartphone", "Tablet", "Celular básico", "Cargador", "Audífonos",
            "Power bank", "Cable / adaptador"
        ),
        "Documento / Paper" to listOf(
            "Documento de identidad", "Carné universitario", "Certificado / papel de oficio",
            "Boleta / nota", "Libreta", "Otro documento"
        ),
        "Llaves" to listOf(
            "Llave de casa", "Llave de vehículo", "Llave de casillero", "Llavero completo"
        ),
        "Tarjeta / Billete" to listOf(
            "Tarjeta bancaria", "Tarjeta de transporte", "Tarjeta de estudiante",
            "Boleto / entrada", "Pulso"
        ),
        "Ropa" to listOf(
            "Buzo / casaca", "Polo / camiseta", "Pantalón", "Zapatos", "Gorra / accesorio"
        ),
        "Libro / Cuaderno" to listOf(
            "Libro de texto", "Cuaderno", "Cuaderno de prácticas", "Agenda", "Carpeta"
        ),
        "Accesorio" to listOf(
            "Bolso / mochila", "Lentes de sol", "Reloj", "Joyería", "Paraguas", "Botella"
        ),
        "Electrodoméstico" to listOf(
            "Audífonos con cable", "Mouse / teclado", "Plancha", "Ventilador", "Enchufe / adaptador"
        ),
        "Joyería" to listOf(
            "Anillo", "Cadena / collar", "Arete", "Pulseras"
        ),
        "Dinero" to listOf(
            "Monedas", "Billetes", "Ambos"
        ),
        "Otro" to listOf(
            "Juguete", "Papelería", "Efecto personal"
        )
    )

    val carreras: List<String> = listOf(
        "Ingeniería de Sistemas",
        "Ingeniería Química e Industrial",
        "Ingeniería Eléctrica y Electrónica",
        "Ingeniería Mecánica",
        "Ingeniería Industrial",
        "Ingeniería de Geología",
        "Ingeniería de Minería",
        "Ingeniería Metalúrgica",
        "Ingeniería Agrónoma",
        "Ingeniería de Telecomunicaciones",
        "Economía",
        "Derecho",
        "Antropología",
        "PSP"
    )

    val catalogosPorCarrera: Map<String, CatalogoCarrera> = linkedMapOf(
        "Ingeniería de Sistemas" to CatalogoCarrera(
            aulas = listOf("401-A (Pabellón B)", "401-B (Pabellón B)", "405-B (Pabellón B)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Sistemas",
                aulas = listOf("101-A (Pabellón B)", "102-A (Pabellón B)", "103-A (Pabellón B)")
            )
        ),
        "Ingeniería Química e Industrial" to CatalogoCarrera(
            aulas = listOf("302-F (Pabellón F)", "310-F (Pabellón F)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Química",
                aulas = listOf("104-F (Pabellón F)", "106-F (Pabellón F)")
            )
        ),
        "Ingeniería Eléctrica y Electrónica" to CatalogoCarrera(
            aulas = listOf("204-C (Pabellón C)", "212-C (Pabellón C)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Electrónica",
                aulas = listOf("101-C (Pabellón C)", "103-C (Pabellón C)")
            )
        ),
        "Ingeniería Mecánica" to CatalogoCarrera(
            aulas = listOf("118-E (Pabellón E)", "126-E (Pabellón E)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Mecánica",
                aulas = listOf("101-E (Pabellón E)", "103-E (Pabellón E)")
            )
        ),
        "Ingeniería Industrial" to CatalogoCarrera(
            aulas = listOf("208-F (Pabellón F)", "214-F (Pabellón F)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Industrial",
                aulas = listOf("102-F (Pabellón F)", "104-F (Pabellón F)")
            )
        ),
        "Ingeniería de Geología" to CatalogoCarrera(
            aulas = listOf("104-G (Pabellón G)", "112-G (Pabellón G)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Geología",
                aulas = listOf("101-G (Pabellón G)", "103-G (Pabellón G)")
            )
        ),
        "Ingeniería de Minería" to CatalogoCarrera(
            aulas = listOf("108-G (Pabellón G)", "116-G (Pabellón G)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Minería",
                aulas = listOf("102-G (Pabellón G)", "104-G (Pabellón G)")
            )
        ),
        "Ingeniería Metalúrgica" to CatalogoCarrera(
            aulas = listOf("120-G (Pabellón G)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Metalurgia",
                aulas = listOf("103-G (Pabellón G)", "105-G (Pabellón G)")
            )
        ),
        "Ingeniería Agrónoma" to CatalogoCarrera(
            aulas = listOf("202-H (Pabellón H)", "210-H (Pabellón H)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Agronomía",
                aulas = listOf("101-H (Pabellón H)", "103-H (Pabellón H)")
            )
        ),
        "Ingeniería de Telecomunicaciones" to CatalogoCarrera(
            aulas = listOf("308-C (Pabellón C)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Telecomunicaciones",
                aulas = listOf("105-C (Pabellón C)", "107-C (Pabellón C)")
            )
        ),
        "Economía" to CatalogoCarrera(
            aulas = listOf("102-A (Pabellón A)", "110-A (Pabellón A)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Economía",
                aulas = listOf("101-A (Pabellón A)", "103-A (Pabellón A)")
            )
        ),
        "Derecho" to CatalogoCarrera(
            aulas = listOf("201-A (Pabellón A)", "209-A (Pabellón A)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Derecho",
                aulas = listOf("105-A (Pabellón A)", "107-A (Pabellón A)")
            )
        ),
        "Antropología" to CatalogoCarrera(
            aulas = listOf("305-A (Pabellón A)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de Antropología",
                aulas = listOf("109-A (Pabellón A)", "111-A (Pabellón A)")
            )
        ),
        "PSP" to CatalogoCarrera(
            aulas = listOf("401-A (Pabellón A)", "405-A (Pabellón A)"),
            laboratorio = Laboratorio(
                nombre = "Laboratorio de PSP",
                aulas = listOf("113-A (Pabellón A)", "115-A (Pabellón A)")
            )
        )
    )

    /** Se usa cuando la carrera no tiene un catálogo propio. */
    val catalogoGeneral = CatalogoCarrera(
        aulas = listOf(
            "401-A (Pabellón B)",
            "210 (Pabellón A)",
            "Aula Magna (Pabellón Central)",
            "Auditorio Central"
        ),
        laboratorio = Laboratorio(
            nombre = "Laboratorio Central",
            aulas = listOf("101-A (Pabellón Central)", "102-A (Pabellón Central)")
        )
    )

    /** Destino de vigilancia derivado de la carrera elegida. */
    private val destinoPorCarrera: Map<String, String> = mapOf(
        "Ingeniería de Sistemas" to "Secretaría de Ing. de Sistemas — Pabellón B",
        "Ingeniería Química e Industrial" to "Secretaría de Ing. Química e Industrial — Pabellón F",
        "Ingeniería Eléctrica y Electrónica" to "Secretaría de Ing. Eléctrica y Electrónica — Pabellón C",
        "Ingeniería Mecánica" to "Secretaría de Ing. Mecánica — Pabellón E",
        "Ingeniería Industrial" to "Secretaría de Ing. Industrial — Pabellón F",
        "Ingeniería de Geología" to "Secretaría de Ing. de Geología — Pabellón G",
        "Ingeniería de Minería" to "Secretaría de Ing. de Minería — Pabellón G",
        "Ingeniería Metalúrgica" to "Secretaría de Ing. Metalúrgica — Pabellón G",
        "Ingeniería Agrónoma" to "Secretaría de Ing. Agrónoma — Pabellón H",
        "Ingeniería de Telecomunicaciones" to "Secretaría de Ing. de Telecomunicaciones — Pabellón C",
        "Economía" to "Secretaría de Economía — Pabellón A",
        "Derecho" to "Secretaría de Derecho — Pabellón A",
        "Antropología" to "Secretaría de Antropología — Pabellón A",
        "PSP" to "Secretaría de PSP — Pabellón A"
    )

    fun tiposDe(categoria: String?): List<String> =
        categoria?.let { tiposPorCategoria[it] }.orEmpty()

    fun catalogoDe(carrera: String?): CatalogoCarrera? =
        carrera?.let { catalogosPorCarrera[it] ?: catalogoGeneral }

    /**
     * Todas las aulas de una carrera: primero las aulas y después las del
     * único laboratorio de esa carrera. Si no hay carrera elegida, la lista
     * queda vacía porque no se puede saber qué aulas corresponden.
     */
    fun aulasDe(carrera: String?): List<AulaOpcion> {
        val catalogo = catalogoDe(carrera) ?: return emptyList()
        val normales = catalogo.aulas.map { AulaOpcion(it, esLaboratorio = false, grupo = GRUPO_AULAS) }
        val delLab = catalogo.laboratorio?.let { lab ->
            lab.aulas.map { AulaOpcion(it, esLaboratorio = true, grupo = lab.nombre) }
        }.orEmpty()
        return normales + delLab
    }

    /** Nombre del laboratorio de la carrera, si tiene. */
    fun laboratorioDe(carrera: String?): String? = catalogoDe(carrera)?.laboratorio?.nombre

    /** Texto para la pantalla de reporte generado. */
    fun destinoVigilancia(carrera: String): String =
        destinoPorCarrera[carrera] ?: "Secretaría de $carrera — Pabellón Central"
}