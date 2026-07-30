package gestorreparaciones.sistema;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import gestorreparaciones.modelo.*;
import gestorreparaciones.enums.*;
import gestorreparaciones.excepciones.*;

public class Sistema {
	private List<Cliente> clientes;
	private List<Empleado> empleados;
	private List<PlantillaDiagnostico> plantillasDiagnostico;
	
	
	public void agregarCliente(Cliente cliente) {
		//TODO// AGREGAR EXCEPCION POR CLIENTE YA EXISTENTE
	}
	public Cliente buscarCliente(String dni) {
		//TODO
		return null;
	}
	public void agregarDispositivo(Cliente cliente, Dispositivo dispositivo) {
		//TODO
	}
	public Reparacion crearReparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
										LocalDate fechaEntregaEstimada) {
		//TODO // AGREGAR EXCEPCION POR LISTA NEGRA
		return null;
	}
	public void marcarListaNegra(Cliente cliente, String motivo, Empleado empleado) {
		//TODO
	}
	public void cambiarEstado(Reparacion reparacion, EstadoReparacion nuevoEstado) {
		//TODO
	}
	public void registrarPago(Reparacion reparacion, Pago pago) {
		//TODO
	}
	public List<Reparacion> listaReparacionPorEstado(EstadoReparacion estado){
		//TODO
		return null;
	}
	
}
