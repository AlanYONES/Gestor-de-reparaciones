package gestorreparaciones.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.enums.EstadoReparacion;

public class Reparacion {
	private int nroOrden;
	private Dispositivo dispositivo;
	private Empleado empleado;
	private EstadoReparacion estado;
	private String fallaDeclarada;
	private String estadoFisicoAlRecibir;
	private String observaciones;
	private String reparacionRealizada;
	private LocalDateTime fechaEntrada;
	private LocalDate fechaEntregaEstimada;
	private LocalDateTime fechaEntregaFinal;
	private double presupuesto;
	private double recargoPorcentaje;
	private List<Pago> pagos;
	private List<String> rutasFotos;
	private List<Integer> patronDesbloqueo;
	private String pinDesbloqueo;
	//TODO 
	public double calculoTotalServicio() {
		return 1;
	}
	public double calcularPendiente() {
		return 1;
	}
}
