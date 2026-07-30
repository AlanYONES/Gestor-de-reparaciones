package gestorreparaciones.modelo;

import java.time.LocalDate;

public class RegistroListaNegra {
	private static int contadorId = 0;
	private final int id;
	private Cliente cliente;
	private String motivo;
	private LocalDate fecha;
	private Empleado empleadoQueRegistro;
	
	public RegistroListaNegra(Cliente cliente, String motivo, LocalDate fecha, Empleado empleadoQueRegistro) {
		this.id = contadorId++;
		this.cliente = cliente;
		this.motivo = motivo;
		this.fecha = fecha;
		this.empleadoQueRegistro = empleadoQueRegistro;
	}

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public Empleado getEmpleadoQueRegistro() {
		return empleadoQueRegistro;
	}
	public void setEmpleadoQueRegistro(Empleado empleadoQueRegistro) {
		this.empleadoQueRegistro = empleadoQueRegistro;
	}
	public int getId() {
		return id;
	}
	
	
}
