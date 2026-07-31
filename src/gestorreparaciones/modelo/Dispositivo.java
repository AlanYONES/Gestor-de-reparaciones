package gestorreparaciones.modelo;

import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.enums.TipoEquipo;

public class Dispositivo {
	private static int contadorId = 0;
	private final int id;
	private Cliente cliente;
	private TipoEquipo tipoEquipo;
	private String marca;
	private String modelo;
	private String imei;
	private String numeroSerie;
	private List<String> accesorios;
	private List<Reparacion> reparaciones;
	
	public Dispositivo(Cliente cliente, TipoEquipo tipoEquipo, String marca, String modelo, String imei, String numeroSerie) {
		this.id = contadorId++;
		this.cliente = cliente;
		this.tipoEquipo = tipoEquipo;
		this.marca = marca;
		this.modelo = modelo;
		this.imei = imei;
		this.numeroSerie = numeroSerie;
		this.accesorios = new ArrayList<>();
		this.reparaciones = new ArrayList<>();
	}

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public TipoEquipo getTipoEquipo() {
		return tipoEquipo;
	}
	public void setTipoEquipo(TipoEquipo tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public int getId() {
		return id;
	}
	public List<String> getAccesorios() {
		return accesorios;
	}
	public List<Reparacion> getReparaciones() {
		return reparaciones;
	}
	
	@Override
	public String toString() {
		return String.format("Dispositivo #%d - Dueño: %s %s - %s -  %s %s - Imei: %s - Nro.Serie: %s", 
								id,cliente.getNombre(), cliente.getApellido(), tipoEquipo, marca, modelo, imei, numeroSerie);
	}
}
