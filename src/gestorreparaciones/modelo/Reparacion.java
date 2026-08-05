package gestorreparaciones.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.enums.EstadoReparacion;

public class Reparacion {
	private static int contadorId = 0;
	private final int id;
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
	private List<Pago> pagos;
	private List<String> rutasFotos;
	private List<Integer> patronDesbloqueo;
	private String pinDesbloqueo;
	private List<HistorialEstado> historialEstados;
	private boolean tieneGarantia;
	private int diasGarantia;
	private LocalDate fechaVencimientoGarantia;
	private boolean canceladaConCargo;
	private double cargoRevision;
	
	public Reparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
						String estadoFisicoAlRecibir, double presupuesto) {
		this.id  = contadorId++;
		this.dispositivo = dispositivo;
		this.empleado = empleado;
		this.estado = EstadoReparacion.RECIBIDO;
		this.fallaDeclarada = fallaDeclarada;
		this.estadoFisicoAlRecibir = estadoFisicoAlRecibir;
		this.reparacionRealizada = null;  // SE COMPLETA AL REALIZAR LA REPARACIÓN
		this.fechaEntrada = LocalDateTime.now();
		this.presupuesto = presupuesto;
		this.pagos = new ArrayList<>();
		this.rutasFotos = new ArrayList<>();
		this.historialEstados = new ArrayList<>();
		this.canceladaConCargo = false;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public EstadoReparacion getEstado() {
		return estado;
	}
	public void setEstado(EstadoReparacion estado) {
		this.estado = estado;
	}
	public String getFallaDeclarada() {
		return fallaDeclarada;
	}
	public void setFallaDeclarada(String fallaDeclarada) {
		this.fallaDeclarada = fallaDeclarada;
	}
	public String getEstadoFisicoAlRecibir() {
		return estadoFisicoAlRecibir;
	}
	public void setEstadoFisicoAlRecibir(String estadoFisicoAlRecibir) {
		this.estadoFisicoAlRecibir = estadoFisicoAlRecibir;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getReparacionRealizada() {
		return reparacionRealizada;
	}
	public void setReparacionRealizada(String reparacionRealizada) {
		this.reparacionRealizada = reparacionRealizada;
	}
	public LocalDateTime getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(LocalDateTime fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public LocalDate getFechaEntregaEstimada() {
		return fechaEntregaEstimada;
	}
	public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) {
		this.fechaEntregaEstimada = fechaEntregaEstimada;
	}
	public LocalDateTime getFechaEntregaFinal() {
		return fechaEntregaFinal;
	}
	public void setFechaEntregaFinal(LocalDateTime fechaEntregaFinal) {
		this.fechaEntregaFinal = fechaEntregaFinal;
	}
	public double getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(double presupuesto) {
		this.presupuesto = presupuesto;
	}
	public List<Pago> getPagos() {
		return pagos;
	}
	public List<String> getRutasFotos() {
		return rutasFotos;
	}
	public List<Integer> getPatronDesbloqueo() {
		return patronDesbloqueo;
	}
	public void setPatronDesbloqueo(List<Integer> patronDesbloqueo) {
		this.patronDesbloqueo = patronDesbloqueo;
	}
	public String getPinDesbloqueo() {
		return pinDesbloqueo;
	}
	public void setPinDesbloqueo(String pinDesbloqueo) {
		this.pinDesbloqueo = pinDesbloqueo;
	}
	public int getId() {
		return id;
	}
	public List<HistorialEstado> getHistorialEstados() {
		return this.historialEstados;
	}
	public boolean isTieneGarantia() {
		return tieneGarantia;
	}
	public void setTieneGarantia(boolean tieneGarantia) {
		this.tieneGarantia = tieneGarantia;
	}
	public int getDiasGarantia() {
		return diasGarantia;
	}
	public void setDiasGarantia(int diasGarantia) {
		this.diasGarantia = diasGarantia;
	}
	public LocalDate getFechaVencimientoGarantia() {
		return fechaVencimientoGarantia;
	}
	public void setFechaVencimientoGarantia(LocalDate fechaVencimientoGarantia) {
		this.fechaVencimientoGarantia = fechaVencimientoGarantia;
	}
	public boolean isCanceladaConCargo() {
		return this.canceladaConCargo;
	}
	public void setCanceladaConCargo(boolean conCargo) {
		this.canceladaConCargo = conCargo;
	}
	public double getCargoRevision() {
		return cargoRevision;
	}
	public void setCargoRevision(double cargoRevision) {
		this.cargoRevision = cargoRevision;
	}

	
	public double calculoTotalServicio() {
		if(estado == EstadoReparacion.CANCELADA) {
			return canceladaConCargo ? cargoRevision : 0.0;
		}
		return this.presupuesto;
	}
	public double calcularPendiente() {
		double totalCobrado = pagos.stream()
									.filter(p -> !p.isPagoInvalido()) // SOLAMENTE TOMA LOS PAGOS QUE NO TENGAN LA FLAG INVALIDO ACTIVA
									.mapToDouble(Pago::getMonto)
									.sum();
		return calculoTotalServicio() - totalCobrado;
	}
	
	@Override
	public String toString() {
		return String.format("Orden #%d - %s %s - Estado: %s - Falla: %s - Entro: %s- Observaciones: %s - "
				+ "Entrega aproximada: %s - Total: $%.2f - Pendiente: $%.2f",
								id, dispositivo.getMarca(), dispositivo.getModelo(), 
								estado, fallaDeclarada, fechaEntrada,observaciones,fechaEntregaEstimada, calculoTotalServicio(), calcularPendiente());
	}
}
