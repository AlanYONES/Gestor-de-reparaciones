package gestorreparaciones.pruebas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import gestorreparaciones.enums.*;
import gestorreparaciones.excepciones.*;
import gestorreparaciones.modelo.*;
import gestorreparaciones.sistema.Sistema;

public class Main {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP: empleados y plantillas de diagnóstico base
        // =========================================================
        Empleado empleado1 = null;
        Empleado empleado2 = null;
        PlantillaDiagnostico plantillaCambioDePantallaOled = null;
        PlantillaDiagnostico plantillaDiagnosticoGeneral = null;
        PlantillaDiagnostico plantillaCambioDeBateria = null;

        try {
            empleado1 = new Empleado("Juan Pérez", "20304050607", RolEmpleado.TECNICO, true);
            empleado2 = new Empleado("Ana García", "20405060708", RolEmpleado.TECNICO, true);
            sistema.agregarEmpleado(empleado1);
            sistema.agregarEmpleado(empleado2);

            plantillaCambioDePantallaOled = new PlantillaDiagnostico(
                "Cambio de pantalla OLED",
                "Reemplazo de módulo de pantalla OLED completo, incluye testeo táctil y de brillo", 3);
            plantillaDiagnosticoGeneral = new PlantillaDiagnostico(
                "Recepción completa / diagnóstico inicial",
                "Cliente solicita diagnóstico técnico general antes de autorizar reparación", 2);
            plantillaCambioDeBateria = new PlantillaDiagnostico(
                "Cambio de batería",
                "Reemplazo de batería original/genérica, incluye testeo de carga", 1);

            System.out.println("--- Setup completo ---");
            sistema.imprimirLista(sistema.getEmpleados());

        } catch (EmpleadoYaExistenteException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // =========================================================
        // 1. ALTAS: clientes, dispositivos, y validación de duplicados
        // =========================================================
        Cliente cliente1 = null;
        Dispositivo dispositivo1 = null;
        Dispositivo dispositivo2 = null;

        try {
            cliente1 = new Cliente("Carlos", "Gómez", TipoDocumento.DNI, "30111222",
                                    "1122334455", "carlos@mail.com");
            sistema.agregarCliente(cliente1);

            dispositivo1 = new Dispositivo(cliente1, TipoEquipo.CELULAR, "Samsung", "A22 5G",
                                            "356789012345678", "SN001");
            dispositivo2 = new Dispositivo(cliente1, TipoEquipo.CELULAR, "Motorola", "G32",
                                            null, "SN002"); // sin IMEI, caso realista
            sistema.agregarDispositivo(cliente1, dispositivo1);
            sistema.agregarDispositivo(cliente1, dispositivo2);
            // SE AGREGAN ACCESORIOS A DISPOSITIVOS
            sistema.agregarAccesorio(dispositivo2, "Funda protectora de Boca");
            sistema.agregarAccesorio(dispositivo2, "Funda protectora de hello kitty");
            
            System.out.println("\n--- Cliente y dispositivos dados de alta ---");
            System.out.println(cliente1);
            sistema.imprimirLista(cliente1.getDispositivos());

            // Caso negativo: cliente duplicado
            Cliente clienteDuplicado = new Cliente("Otro", "Nombre", TipoDocumento.DNI, "30111222",
                                                     "111", "otro@mail.com");
            sistema.agregarCliente(clienteDuplicado);

        } catch (ClienteYaExistenteException | DispositivoDuplicadoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }

        // =========================================================
        // 2. REPARACIONES: creación, plantillas, empleado inactivo
        // =========================================================
        Reparacion reparacion1 = null;
        try {
            reparacion1 = sistema.crearReparacion(dispositivo1, empleado1,
                                                    "No da imagen, solo lineas verticales",
                                                    "Pantalla astillada, no usable, carga (1.2A)", 60000.0);
            sistema.aplicarPlantilla(reparacion1, plantillaCambioDePantallaOled);
            System.out.println("\n--- Reparación creada con plantilla aplicada ---");
            System.out.println(reparacion1);
            System.out.println("\n--- Se agrega fotografia del equipo en: ---");
            sistema.agregarRutaFoto(reparacion1, "C:/user/Alan/Images/test");
            sistema.imprimirLista(reparacion1.getRutasFotos());
            // Caso negativo: empleado inactivo
            sistema.darBajaEmpleado(empleado2);
            sistema.crearReparacion(dispositivo2, empleado2, "Test empleado inactivo",
                                     "N/A", 10000.0);

        } catch (EmpleadoInactivoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
            sistema.darAltaEmpleado(empleado2); // lo reactivamos para el resto de las pruebas
        }

        // =========================================================
        // 3. CAMBIOS DE ESTADO E HISTORIAL
        // =========================================================
        sistema.cambiarEstado(reparacion1, EstadoReparacion.EN_REPARACION, empleado1);
        sistema.cambiarEstado(reparacion1, EstadoReparacion.ENTREGADO, empleado1);

        System.out.println("\n--- Historial de estados de reparacion1 ---");
        sistema.imprimirLista(reparacion1.getHistorialEstados());

        // =========================================================
        // 4. GARANTÍA
        // =========================================================
        sistema.asignarGarantia(reparacion1, 30);
        System.out.println("\n--- Garantía asignada ---");
        System.out.println("Vence: " + reparacion1.getFechaVencimientoGarantia());
        System.out.println(sistema.verificacionGarantia(dispositivo1));

        System.out.println("\n--- Dispositivos en garantía ---");
        sistema.imprimirLista(sistema.dispositivosEnGarantia());

        // =========================================================
        // 5. PAGOS: normal, con recargo, exceso, anulación
        // =========================================================
        try {
            // Pago parcial en efectivo (seña)
            Pago sena = new Pago(reparacion1, 30000.0, LocalDate.now(),FormaDePago.EFECTIVO, TipoPago.SEÑA);
            sistema.registrarPago(reparacion1, sena);
            System.out.println("\n--- Pago de seña registrado ---");
            System.out.println("Pendiente: " + reparacion1.calcularPendiente());

            // Pago con recargo (tarjeta)
            double recargoSugerido = sistema.sugerirRecargo(FormaDePago.TARJETA);
            Pago saldoTarjeta = new Pago(reparacion1, 30000.0, LocalDate.now(),FormaDePago.TARJETA, TipoPago.SALDO);
            saldoTarjeta.setRecargoPorcentaje(recargoSugerido);
            sistema.registrarPago(reparacion1, saldoTarjeta);
            System.out.println("\n--- Pago con tarjeta registrado ---");
            System.out.println("Monto con recargo (informativo): " + saldoTarjeta.montoConRecargo());
            System.out.println("Pendiente: " + reparacion1.calcularPendiente());

            // Caso negativo: pago que excede el saldo
            Pago pagoInvalido = new Pago(reparacion1, 99999.0, LocalDate.now(),FormaDePago.EFECTIVO, TipoPago.SALDO);
            sistema.registrarPago(reparacion1, pagoInvalido);

        } catch (PagoInvalidoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }

        // Anulación de pago
        sistema.anularPago(reparacion1.getPagos().get(0));
        System.out.println("\n--- Pago anulado ---");
        System.out.println("Pendiente tras anular seña: " + reparacion1.calcularPendiente());

        // =========================================================
        // 6. LISTA NEGRA
        // =========================================================
        sistema.marcarListaNegra(cliente1, "No retira equipos hace más de 6 meses", empleado1);
        System.out.println("\n--- Cliente en lista negra ---");
        sistema.imprimirLista(sistema.filtroClientesEnListaNegra());

        sistema.quitarDeListaNegra(cliente1);
        System.out.println("¿Sigue en lista negra? " + cliente1.isEnListaNegra());
        System.out.println("Historial de conflictos (se mantiene): ");
        sistema.imprimirLista(cliente1.getListaConflictos());
        
        // =========================================================
        // 7. CANCELACIÓN DE REPARACIONES (con y sin cargo)
        // =========================================================
        try {
            Reparacion reparacionSinArreglo = sistema.crearReparacion(dispositivo2, empleado1,
                                                    "No tiene arreglo, placa quemada",
                                                    "Manchas de corrosión visibles", 50000.0);
            sistema.cancelarReparacion(reparacionSinArreglo, false, 0.0);
            System.out.println("\n--- Cancelada sin cargo ---");
            System.out.println("Total servicio: " + reparacionSinArreglo.calculoTotalServicio());

            Reparacion reparacionConRevision = sistema.crearReparacion(dispositivo1, empleado1,
                                                    "Cliente no quiere reparar", "N/A", 40000.0);
            sistema.cancelarReparacion(reparacionConRevision, true, 2500.0);
            System.out.println("\n--- Cancelada con cargo de revisión ---");
            System.out.println("Total servicio: " + reparacionConRevision.calculoTotalServicio());

        } catch (EmpleadoInactivoException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // =========================================================
        // 8. BÚSQUEDAS PUNTUALES
        // =========================================================
        try {
            System.out.println("\n--- Búsquedas ---");
            System.out.println(sistema.buscarCliente("30111222"));
            sistema.buscarCliente("00000000"); // no encontrado
        } catch (ClienteNoEncontradoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }

        try {
            System.out.println(sistema.buscarReparacionPorId(reparacion1.getId()));
            sistema.buscarReparacionPorId(9999); // no encontrado
        } catch (ReparacionNoEncontradaException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }

        System.out.println("Por modelo (A22 5G): ");
        sistema.imprimirLista(sistema.buscarDispositivoPorModelo("A22 5G"));
        
        // ENCUENTRA POR IMEI
        try {
            System.out.println(sistema.buscarDispositivoPorImei("356789012345678"));
        } catch (DispositivoNoEncontradoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }
        
        // NO ENCUENTRA POR IMEI
        try {
            System.out.println(sistema.buscarDispositivoPorImei("356789012345679"));
        } catch (DispositivoNoEncontradoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        }
        
        try {
        	System.out.println("Busqueda de empleado:");
        	System.out.println(sistema.buscarEmpleado("20304050607"));
        	System.out.println(sistema.buscarEmpleado("20405060708"));
        	System.out.println(sistema.buscarEmpleado("0")); // NO EXISTE
        	System.out.println(sistema.buscarEmpleado(null)); // CUIT NULL
        }catch(EmpleadoNoEncontradoException e) {
        	System.out.println("Exception esperada: " + e.getMessage());
        }
        // =========================================================
        // 9. LISTADOS Y FILTROS
        // =========================================================
        System.out.println("\n--- Reparaciones por estado (ENTREGADO) ---");
        sistema.imprimirLista(sistema.listaReparacionPorEstado(EstadoReparacion.ENTREGADO));

        try {
            System.out.println("\n--- Reparaciones del cliente1 ---");
            sistema.imprimirLista(sistema.listaReparacionesPorCliente("30111222"));
        } catch (ClienteNoEncontradoException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // =========================================================
        // 10. REPORTES Y ESTADÍSTICAS
        // =========================================================
        System.out.println("\n--- Reparaciones vencidas ---");
        sistema.imprimirLista(sistema.reparacionesVencidas());

        System.out.println("\n--- Cantidad de reparaciones por estado ---");
        Map<EstadoReparacion, Long> resumen = sistema.cantidadReparacionesPorEstado();
        for (EstadoReparacion estado : EstadoReparacion.values()) {
            System.out.println(estado + ": " + resumen.getOrDefault(estado, 0L));
        }

        System.out.println("\n--- Empleado con más reparaciones ---");
        System.out.println(sistema.empleadoConMasReparaciones());

        System.out.println("\n--- Ranking de empleados (ascendente) ---");
        sistema.imprimirLista(sistema.listadoEmpleadosPorReparacionesAscendente());

        System.out.println("\n--- Total recaudado (últimos 30 días) ---");
        double recaudado = sistema.totalRecaudadoPorRangoFecha(LocalDate.now().minusDays(30), LocalDate.now());
        System.out.println("$" + recaudado);
    }
}