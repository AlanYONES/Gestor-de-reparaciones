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
}
