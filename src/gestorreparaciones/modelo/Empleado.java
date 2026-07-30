package gestorreparaciones.modelo;
import gestorreparaciones.enums.RolEmpleado;

public class Empleado {
	private static int contadorId = 0;
	private final int id;
	private String nombre;
	private RolEmpleado rol;
	private boolean activo;
	
	public Empleado(String nombre, RolEmpleado rol, boolean activo) {
		this.id = contadorId++;
		this.nombre = nombre;
		this.rol = rol;
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public RolEmpleado getRol() {
		return rol;
	}
	public void setRol(RolEmpleado rol) {
		this.rol = rol;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public int getId() {
		return id;
	}
	
	
}
