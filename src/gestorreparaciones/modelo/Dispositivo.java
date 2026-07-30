package gestorreparaciones.modelo;

import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.enums.TipoEquipo;

public class Dispositivo {
	private int id;
	private Cliente cliente;
	private TipoEquipo tipoEquipo;
	private String marca;
	private String modelo;
	private String imei;
	private String numeroSerie;
	private List<String> accesorios;
	private List<Reparacion> reparaciones;
}
