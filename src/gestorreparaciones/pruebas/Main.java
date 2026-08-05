package gestorreparaciones.pruebas;

import gestorreparaciones.sistema.Sistema;
import gestorreparaciones.modelo.*;

import java.time.LocalDate;

import gestorreparaciones.enums.*;
import gestorreparaciones.excepciones.*;


public class Main {

	public static void main(String[] args) {
		// PRUEBAS PARTE 1: ALTAS
		// CREO SISTEMA
		Sistema sistema = new Sistema();
		
		// CREO Y AGREGO CLIENTE A SISTEMA
		Cliente cliente1 = new Cliente("Alan", "Yones", TipoDocumento.DNI,
				"11111111","1144442222", "correoexample@gmail.com" );
		try {
			sistema.agregarCliente(cliente1);
		} catch (ClienteYaExistenteException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// AGREGO MISMO CLIENTE AL SISTEMA
		try {
			sistema.agregarCliente(cliente1);
		} catch (ClienteYaExistenteException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// CREO Y AGREGO EMPLEADO A SISTEMA
		Empleado empleado1 = new Empleado("Alan", "2311111119",RolEmpleado.TECNICO, true);
		try {
			sistema.agregarEmpleado(empleado1);
		} catch (EmpleadoYaExistenteException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// AGREGO MISMO EMPLEADO AL SISTEMA
		try {
			sistema.agregarEmpleado(empleado1);
		} catch (EmpleadoYaExistenteException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		// CREO Y AGREGO DISPOSITIVO A CLIENTE
		Dispositivo dispositivo1 = new Dispositivo(cliente1,TipoEquipo.CELULAR,"Samsung","A21s","111111111111111","SM-A217M");
		try {
			sistema.agregarDispositivo(cliente1, dispositivo1);
		} catch (DispositivoDuplicadoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// AGREGO MISMO DISPOSITIVO A CLIENTE
		try {
			sistema.agregarDispositivo(cliente1, dispositivo1);
		} catch (DispositivoDuplicadoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		// CREO REPARACION Y APLICO PLANTILLA DIAGNOSTICO
		
		PlantillaDiagnostico plantillaCambioDePantallaOled = new PlantillaDiagnostico("Cambio de modulo de pantalla", "Se realiza cambio de pantalla de calidad OLED (no original)", 1);
		/*try {
		Reparacion reparacion1 = sistema.crearReparacion(dispositivo1, empleado1, "No da imagen, solo lineas verticales", "Pantalla astillada, no usable, carga (1.2A)", 60000.0);
		sistema.aplicarPlantilla(reparacion1, plantillaCambioDePantallaOled);
		System.out.println(reparacion1);
		} catch (EmpleadoInactivoException e) {
			System.out.println("Exception" + e.getMessage());
		}*/
		
		// CREO REPARACION CON EMPLEADO INACTIVO
		Empleado empleado2 = new Empleado("Carlos", "23222222229",RolEmpleado.TECNICO, false);
		try {
			Reparacion reparacion2 = sistema.crearReparacion(dispositivo1, empleado2, "PRUEBA", "PRUEBA", 1.0);
		}catch(EmpleadoInactivoException e) {
			System.out.println("Exception:" + e.getMessage());
		}
		// CAMBIO ESTADO DE UNA REPARACION, VERIFICO QUE SE AGREGUIE A HISTORIAL Y APLICO GARANTIA
		/*try {
			Reparacion reparacion1 = sistema.crearReparacion(dispositivo1, empleado1, "No da imagen, solo lineas verticales", "Pantalla astillada, no usable, carga (1.2A)", 60000.0);
			sistema.aplicarPlantilla(reparacion1, plantillaCambioDePantallaOled);
			sistema.cambiarEstado(reparacion1, EstadoReparacion.EN_REPARACION, empleado1); //CAMBIO ESTADO A EN REPARACION
			System.out.println(reparacion1.getHistorialEstados());
			sistema.cambiarEstado(reparacion1, EstadoReparacion.ENTREGADO, empleado1); // CAMBIO ESTADO A ENTREGADO
			sistema.imprimirLista(reparacion1.getHistorialEstados()); // CHEQUEO QUE SE GUARDEN LOS ESTADOS
			System.out.println(reparacion1.getFechaEntregaFinal());  // CHEQUEO QUE SE GUARDE FECHA DE ENTREGA FINAL
			System.out.println(reparacion1.isTieneGarantia());	// CHEQUEO BOOLEAN DE GARANTIA PREVIO A APLICARLA
			sistema.asignarGarantia(reparacion1, 30);	// APLICO GARANTIA
			System.out.println(reparacion1.isTieneGarantia());	// CHEQUEO GARANTIA DEVUELTA
			System.out.println(reparacion1.getFechaVencimientoGarantia());	// CONFIRMO QUE LA FECHA ESTÉ BIEN
			sistema.imprimirLista(reparacion1.getHistorialEstados());
			} catch (EmpleadoInactivoException e) {
				System.out.println("Exception" + e.getMessage());
			}*/
		// REGISTRAR PAGO PARCIAL DE UNA REPARACION, VERIFICAR PENDIENTE, REGISTRAR PAGO CON RECARGO (TARJETA) 
		// USANDO "sugerirRecargo", VERIFICAR MONTO CON RECARGO SEA CORRECTO
		// REGISTRAR PAGO QUE EXCEDA TOTAL
		// COMPLETAR UN PAGO (SALDO DEBE QUEDAR EN $0)
		// ANULAR UN PAGO, VERIFICAR QUE EL PENDIENTE VUELVA A SUBIR
		/*
		try {
			Reparacion reparacion1 = sistema.crearReparacion(dispositivo1, empleado1, "No da imagen, solo lineas verticales", "Pantalla astillada, no usable, carga (1.2A)", 60000.0);
			try {
				Pago pagoEfectivo = new Pago(reparacion1,30000.0,LocalDate.now(),FormaDePago.EFECTIVO,TipoPago.SEÑA);
				sistema.registrarPago(reparacion1, pagoEfectivo);
				System.out.println("Restante de la reparacion: "+reparacion1.calcularPendiente());
				double recargoSugerido = sistema.sugerirRecargo(FormaDePago.TARJETA);
				System.out.println("Recargo sugerido para tarjeta: " + recargoSugerido + "%");
				Pago pagoConTarjeta = new Pago(reparacion1,30000.0,LocalDate.now(),FormaDePago.TARJETA,TipoPago.SALDO);
				pagoConTarjeta.setRecargoPorcentaje(recargoSugerido);
				System.out.println("Monto base: " + pagoConTarjeta.getMonto());
				System.out.println("Monto con recargo: " + pagoConTarjeta.montoConRecargo());
				sistema.registrarPago(reparacion1, pagoConTarjeta);
			    System.out.println("Pago registrado. Pendiente actual: " + reparacion1.calcularPendiente());
			    sistema.anularPago(pagoConTarjeta);
				System.out.println("Pago anulado, restante de la reparacion: "+reparacion1.calcularPendiente());
				
			}catch (PagoInvalidoException e) {
				System.out.println("Exception: "+e.getMessage());
			}
		}catch (EmpleadoInactivoException e) {
			
		}*/
		
		// MARCO CLIENTE EN LISTA NEGRA
		Cliente cliente2 = new Cliente("Carlos", "Carlos", TipoDocumento.DNI,
				"33333333","1144442222", "correoexample@gmail.com" );
		try {
			sistema.agregarCliente(cliente2);
			System.out.println(cliente2.isEnListaNegra()); //VERIFICO SI ESTA EN LISTA NEGRA
			sistema.marcarListaNegra(cliente2, "PRUEBA", empleado1); // LO MARCO EN LISTA NEGRA
			System.out.println(cliente2.isEnListaNegra()); //VERIFICO FLAG
			sistema.imprimirLista(sistema.filtroClientesEnListaNegra()); // CHEQUEO FILTRO
			sistema.quitarDeListaNegra(cliente2); // QUITO DE LISTA NEGRA
			sistema.imprimirLista(cliente2.getListaConflictos()); // CHEQUEO QUE SIGA REGISTRADO EL CONFLICTO
			
		} catch (ClienteYaExistenteException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// PRUEBO CANCELAR REPARACION CON Y SIN RECARGO, VERIFICO IMPACTO EN PAGOS Y REPORTE DE INGRESOS
		Dispositivo dispositivo2 = new Dispositivo(cliente1,TipoEquipo.CELULAR,"Motorola", "G5","0", "0");
		try {
			sistema.agregarDispositivo(cliente1, dispositivo2);
		} catch (DispositivoDuplicadoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		try {
			Reparacion reparacionSinArreglo = sistema.crearReparacion(dispositivo2, empleado1, "No enciende",
                "Golpes visibles en carcasa", 5000.0);
			sistema.cancelarReparacion(reparacionSinArreglo, false, 0.0);
			System.out.println("Estado: " + reparacionSinArreglo.getEstado());
			System.out.println("Con cargo: " + reparacionSinArreglo.isCanceladaConCargo());
			System.out.println("Total servicio: " + reparacionSinArreglo.calculoTotalServicio());
		}catch(EmpleadoInactivoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		try {
			Reparacion reparacionConRevision = sistema.crearReparacion(dispositivo2, empleado1, "Se moja, no prende",
                    "Manchas de humedad visibles", 7000.0);

			sistema.cancelarReparacion(reparacionConRevision, true, 2500.0);
			System.out.println("Estado: " + reparacionConRevision.getEstado());
			System.out.println("Con cargo: " + reparacionConRevision.isCanceladaConCargo());
			System.out.println("Cargo revision: " + reparacionConRevision.getCargoRevision());
			System.out.println("Total servicio: " + reparacionConRevision.calculoTotalServicio());   // debería dar 2500.0, no 7000.0
			double recaudado = sistema.totalRecaudadoPorRangoFecha(LocalDate.now().minusDays(1), LocalDate.now());
			System.out.println("Total recaudado: " + recaudado);
		}catch(EmpleadoInactivoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		//PRUEBA DE BUSQUEDA DE CLIENTE, EXISTENTE E INEXISTENTE
		try {
			System.out.println(sistema.buscarCliente("11111111")); // CLIENTE EXISTE
			System.out.println(sistema.buscarCliente("44444444")); // CLIENTE INEXISTENTE, DA EXCEPTION
		}catch(ClienteNoEncontradoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		// PRUEBA DE BUSQUEDA DE DISPOSITIVO POR MODELO Y POR IMEI
		sistema.imprimirLista(sistema.buscarDispositivoPorModelo("A21S"));
		try {
			System.out.println(sistema.buscarDispositivoPorImei("111111111111111"));
			System.out.println(sistema.buscarDispositivoPorImei("0"));
			System.out.println(sistema.buscarDispositivoPorImei("?"));
		}catch(DispositivoNoEncontradoException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		// BUSCAR REPARACION POR ID
		try {
			Reparacion reparacion1 = sistema.crearReparacion(dispositivo1, empleado1, "No da imagen, solo lineas verticales", "Pantalla astillada, no usable, carga (1.2A)", 60000.0);
			sistema.aplicarPlantilla(reparacion1, plantillaCambioDePantallaOled);
			System.out.println(reparacion1);
			try {
				System.out.println(sistema.buscarReparacionPorId(0));
				System.out.println(sistema.buscarReparacionPorId(4));
			}catch(ReparacionNoEncontradaException e) {
				System.out.println("Exception: " + e.getMessage());
			}
			} catch (EmpleadoInactivoException e) {
				System.out.println("Exception" + e.getMessage());
			}
		sistema.imprimirLista(sistema.listaReparacionPorEstado(EstadoReparacion.RECIBIDO));
		sistema.imprimirLista(sistema.listaReparacionPorEstado(EstadoReparacion.ENTREGADO));
		sistema.imprimirLista(sistema.listaReparacionPorEstado(EstadoReparacion.CANCELADA));
		//sistema.imprimirLista(sistema.);
	}
	
}
