package gestorreparaciones.modelo;

public class PlantillaDiagnostico {
	private int id;
	private String nombre;
	private String descripcion;
	private int diasEstimados;
	
	public PlantillaDiagnostico(String nombre, String descripcion, int diasEstimados) {
		this.id = -1;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.diasEstimados = diasEstimados;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getDiasEstimados() {
		return diasEstimados;
	}
	public void setDiasEstimados(int diasEstimados) {
		this.diasEstimados = diasEstimados;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
