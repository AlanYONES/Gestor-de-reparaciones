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
	private double recargoPorcentaje;
	private List<Pago> pagos;
	private List<String> rutasFotos;
	private List<Integer> patronDesbloqueo;
	private String pinDesbloqueo;
	
	public Reparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
						String estadoFisicoAlRecibir, String observaciones, String reparacionRealizada,
							LocalDateTime fechaEntrada, LocalDate fechaEntregaEstimada, double presupuesto, 
								double recargoPorcentaje) {
		this.id  = contadorId++;
		this.dispositivo = dispositivo;
		this.empleado = empleado;
		this.estado = EstadoReparacion.RECIBIDO;
		this.fallaDeclarada = fallaDeclarada;
		this.estadoFisicoAlRecibir = estadoFisicoAlRecibir;
		this.observaciones = observaciones;
		this.reparacionRealizada = reparacionRealizada;
		this.fechaEntrada = fechaEntrada;
		this.fechaEntregaEstimada = fechaEntregaEstimada;
		this.presupuesto = presupuesto;
		this.recargoPorcentaje = recargoPorcentaje;
		this.pagos = new ArrayList<>();
		this.rutasFotos = new ArrayList<>();
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
	public double getRecargoPorcentaje() {
		return recargoPorcentaje;
	}
	public void setRecargoPorcentaje(double recargoPorcentaje) {
		this.recargoPorcentaje = recargoPorcentaje;
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

	public double calculoTotalServicio() {
		double recargo = this.presupuesto * (this.recargoPorcentaje / 100.0);
		return this.presupuesto + recargo;
	}
	public double calcularPendiente() {
		double totalCobrado = 0;
		for (Pago pago : pagos) {
			totalCobrado += pago.getMonto();
		}	
		return calculoTotalServicio() - totalCobrado;
	}
}
