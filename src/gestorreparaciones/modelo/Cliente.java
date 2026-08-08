package gestorreparaciones.modelo;

import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.enums.TipoDocumento;

public class Cliente {
	private int id;
	private String nombre;
	private String apellido;
	private TipoDocumento tipoDocumento;
	private String numeroDocumento;
	private String telefono;
	private String correo;
	private List<RegistroListaNegra> listaConflictos;
	private List<Dispositivo> dispositivos;
	private boolean enListaNegra;
	
	public Cliente(String nombre, String apellido, TipoDocumento tipoDocumento, String numeroDocumento,
			String telefono, String correo) {
		super();
		this.id = -1;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.telefono = telefono;
		this.correo = correo;
		this.listaConflictos = new ArrayList<>();
		this.dispositivos = new ArrayList<>();
		this.enListaNegra = false;
	}
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return this.apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getTelefono() {
		return this.telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return this.correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public List<RegistroListaNegra> getListaConflictos(){
		return this.listaConflictos;
	}
	public List<Dispositivo> getDispositivos(){
		return this.dispositivos;
	}
	public boolean isEnListaNegra() {
		return this.enListaNegra;
	}
	public void setEnListaNegra(boolean estado) {
		this.enListaNegra = estado;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Cliente otro = (Cliente) obj;
		return this.numeroDocumento.equals(otro.getNumeroDocumento());
	}
	
	@Override
	public String toString() {
		return String.format("Cliente #%d - %s %s - %s: %s - Tel: %s - Correo: %s",
								id, nombre, apellido, tipoDocumento, numeroDocumento, telefono, correo);
				
	}
}
