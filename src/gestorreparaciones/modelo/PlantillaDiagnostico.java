package gestorreparaciones.modelo;

public class PlantillaDiagnostico {
	private static int contadorId = 0;
	private final int id;
	private String nombre;
	private String descripcion;
	private int diasEstimados;
	
	public PlantillaDiagnostico(String nombre, String descripcion, int diasEstimados) {
		this.id = contadorId++;
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
	
	
}
