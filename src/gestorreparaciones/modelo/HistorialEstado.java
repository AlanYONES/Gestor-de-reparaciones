package gestorreparaciones.modelo;

import java.time.LocalDateTime;
import gestorreparaciones.enums.EstadoReparacion;

public class HistorialEstado {
	private static int contadorId = 0;
	private final int id;
	private Reparacion reparacion;
	private EstadoReparacion estado;
	private LocalDateTime fecha;
	private Empleado empleado;
	
	public HistorialEstado(Reparacion reparacion, EstadoReparacion estado, Empleado empleado) {
		this.id = contadorId++;
		this.reparacion = reparacion;
		this.estado = estado;
		this.fecha = LocalDateTime.now();
		this.empleado = empleado;
	}

	public int getId() {
		return id;
	}
	public Reparacion getReparacion() {
		return reparacion;
	}
	public EstadoReparacion getEstado() {
		return estado;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	
}
