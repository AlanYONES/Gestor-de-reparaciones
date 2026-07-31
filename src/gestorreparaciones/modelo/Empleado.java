package gestorreparaciones.modelo;
import gestorreparaciones.enums.RolEmpleado;

public class Empleado {
	private static int contadorId = 0;
	private final int id;
	private String nombre;
	private String cuit;
	private RolEmpleado rol;
	private boolean activo;
	
	public Empleado(String nombre, String cuit,RolEmpleado rol, boolean activo) {
		this.id = contadorId++;
		this.nombre = nombre;
		this.cuit = cuit;
		this.rol = rol;
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCuit() {
		return this.cuit;
	}
	public void setCuit(String nuevoCuit) {
		this.cuit = nuevoCuit;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Empleado otro = (Empleado) obj;
		return this.cuit.equals(otro.getCuit());
	}
	
	@Override
	public String toString() {
		return String.format("Empleado #%d - %s - Cuit: %s - %s", 
								id, nombre, cuit, rol);
	}
}
