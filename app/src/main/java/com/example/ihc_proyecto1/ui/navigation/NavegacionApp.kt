package com.example.ihc_proyecto1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ihc_proyecto1.data.model.ResultadoEnvio
import com.example.ihc_proyecto1.ui.Factorias
import com.example.ihc_proyecto1.ui.screens.detalle.DetalleReporteViewModel
import com.example.ihc_proyecto1.ui.screens.detalle.PantallaDetalleReporte
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaCapturaFoto
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaCustodia
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaDatosObjeto
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaEnviando
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaReporteGenerado
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaResumen
import com.example.ihc_proyecto1.ui.screens.flujo.PantallaRevisarFoto
import com.example.ihc_proyecto1.ui.screens.flujo.ReportarViewModel
import com.example.ihc_proyecto1.ui.screens.inicio.InicioViewModel
import com.example.ihc_proyecto1.ui.screens.inicio.PantallaInicio
import com.example.ihc_proyecto1.ui.screens.misreportes.MisReportesViewModel
import com.example.ihc_proyecto1.ui.screens.misreportes.PantallaMisReportes

@Composable
fun NavegacionApp(navController: NavHostController = rememberNavController()) {
    // Un solo ViewModel para todo el flujo de reporte (compartido por las pantallas).
    val reporte: ReportarViewModel = viewModel(factory = Factorias.General)
    val borrador by reporte.estado.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Rutas.INICIO) {

        composable(Rutas.INICIO) {
            val vm: InicioViewModel = viewModel(factory = Factorias.General)
            val estado by vm.estado.collectAsStateWithLifecycle()

            PantallaInicio(
                estado = estado,
                onReportar = { navController.navigate(Rutas.Flujo.CAPTURAR) },
                onMisReportes = { navController.navigate(Rutas.MIS_REPORTES) },
                onCambiarModoSinConexion = vm::cambiarModoSinConexion,
                onCambiarSimulacionFallo = vm::cambiarSimulacionFallo
            )
        }

        composable(Rutas.MIS_REPORTES) {
            val vm: MisReportesViewModel = viewModel(factory = Factorias.General)
            val lista by vm.reportes.collectAsStateWithLifecycle()

            PantallaMisReportes(
                reportes = lista,
                onAtras = { navController.popBackStack() },
                onVerDetalle = { id -> navController.navigate(Rutas.detalleReporte(id)) }
            )
        }

        composable(
            route = Rutas.DETALLE_REPORTE,
            arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.StringType })
        ) {
            val vm: DetalleReporteViewModel = viewModel(factory = Factorias.General)
            val dato by vm.reporte.collectAsStateWithLifecycle()

            PantallaDetalleReporte(reporte = dato, onAtras = { navController.popBackStack() })
        }

        // ---------- Flujo de reporte ----------

        composable(Rutas.Flujo.CAPTURAR) {
            PantallaCapturaFoto(
                onFotoCapturada = { ruta ->
                    reporte.fotoCapturada(ruta)
                    navController.navigate(Rutas.Flujo.REVISAR) {
                        popUpTo(Rutas.Flujo.CAPTURAR) { inclusive = true }
                    }
                },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(Rutas.Flujo.REVISAR) {
            PantallaRevisarFoto(
                rutaFoto = borrador.fotoPath,
                onRepetir = {
                    reporte.descartarFoto()
                    navController.navigate(Rutas.Flujo.CAPTURAR) {
                        popUpTo(Rutas.Flujo.REVISAR) { inclusive = true }
                    }
                },
                onContinuar = { navController.navigate(Rutas.Flujo.DATOS) }
            )
        }

        composable(Rutas.Flujo.DATOS) {
            PantallaDatosObjeto(
                estado = borrador,
                onCategoria = reporte::cambiarCategoria,
                onTipo = reporte::cambiarTipo,
                onCarrera = reporte::cambiarCarrera,
                onAula = reporte::elegirAula,
                onEscribirAula = reporte::usarAulaPersonalizada,
                onTextoAula = reporte::cambiarAula,
                onNota = reporte::cambiarNota,
                onCambiarFoto = {
                    navController.navigate(Rutas.Flujo.REVISAR) {
                        popUpTo(Rutas.Flujo.DATOS) { inclusive = true }
                    }
                },
                onTomarFoto = { navController.navigate(Rutas.Flujo.CAPTURAR) },
                onContinuar = {
                    if (reporte.validar()) navController.navigate(Rutas.Flujo.RESUMEN)
                }
            )
        }

        composable(Rutas.Flujo.RESUMEN) {
            PantallaResumen(
                estado = borrador,
                onEditarFoto = {
                    navController.navigate(Rutas.Flujo.REVISAR) {
                        popUpTo(Rutas.Flujo.RESUMEN) { inclusive = true }
                    }
                },
                onEditarDatos = {
                    navController.navigate(Rutas.Flujo.DATOS) {
                        popUpTo(Rutas.Flujo.RESUMEN) { inclusive = true }
                    }
                },
                onEnviar = { navController.navigate(Rutas.Flujo.ENVIANDO) }
            )
        }

        composable(Rutas.Flujo.ENVIANDO) {
            val irAGenerado = {
                navController.navigate(Rutas.Flujo.GENERADO) {
                    popUpTo(Rutas.Flujo.ENVIANDO) { inclusive = true }
                }
            }

            PantallaEnviando(
                hayConexion = borrador.hayConexion,
                errorEnvio = borrador.errorEnvio,
                hayReporteGuardado = borrador.pin != null,
                onEnviar = {
                    reporte.enviar { resultado ->
                        if (resultado !is ResultadoEnvio.Fallido) irAGenerado()
                    }
                },
                onReintentar = {
                    reporte.reintentarEnvio { resultado ->
                        if (resultado !is ResultadoEnvio.Fallido) irAGenerado()
                    }
                },
                onContinuarGuardado = irAGenerado
            )
        }

        composable(Rutas.Flujo.GENERADO) {
            PantallaReporteGenerado(
                estado = borrador,
                onIrACustodia = {
                    navController.navigate(Rutas.Flujo.CUSTODIA) {
                        popUpTo(Rutas.Flujo.GENERADO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Flujo.CUSTODIA) {
            PantallaCustodia(
                estado = borrador,
                onVolverAlInicio = {
                    reporte.reiniciar()
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }
    }
}