package gestorreparaciones.sistema;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import gestorreparaciones.modelo.*;
import gestorreparaciones.enums.*;
import gestorreparaciones.excepciones.*;

public class Sistema {
	private List<Cliente> clientes;
	private List<Empleado> empleados;
	private List<PlantillaDiagnostico> plantillasDiagnostico;
	
	
	public void agregarCliente(Cliente cliente)throws ClienteYaExistenteException{
		for(Cliente c : clientes) {
			if(c.equals(cliente)) {
				throw new ClienteYaExistenteException("Ya existe un cliente con documento " + cliente.getNumeroDocumento(), " Documento duplicado.");
			}
		}
		clientes.add(cliente);
	}
	
	
	public Cliente buscarCliente(String dni)throws ClienteNoEncontradoException {
		return clientes.stream()
						.filter(c -> c.getNumeroDocumento().equals(dni))
						.findFirst()
						.orElseThrow(() -> new ClienteNoEncontradoException("No se encontro cliente con documento: " + dni));
	}
	
	
	public void agregarDispositivo(Cliente cliente, Dispositivo dispositivo)throws DispositivoDuplicadoException {
		boolean duplicado = cliente.getDispositivos().stream()
														.anyMatch(c -> dispositivo.getImei() != null
																	&& dispositivo.getImei().equals(c.getImei()));
		if(duplicado) {
			throw new DispositivoDuplicadoException("Ya existe un dispositivo con imei: " + dispositivo.getImei());
		}	
		cliente.getDispositivos().add(dispositivo);
	}
	
	
	public Reparacion crearReparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
										String estadoFisicoAlRecibir, String observaciones, LocalDate fechaEntregaEstimada, 
											double presupuesto)throws EmpleadoInactivoException {
		if(!empleado.isActivo()) {
			throw new EmpleadoInactivoException("El empleado " + empleado.getNombre() + " no está activo y no puede recibir reparaciones.");
		}
		Reparacion retorno = new Reparacion(dispositivo, empleado, fallaDeclarada, 
												estadoFisicoAlRecibir, observaciones, 
													fechaEntregaEstimada, presupuesto);
		dispositivo.getReparaciones().add(retorno);
		return retorno;
	}
	
	
	public void marcarListaNegra(Cliente cliente, String motivo,Empleado empleado) {
		RegistroListaNegra nuevoRegistro = new RegistroListaNegra(cliente, motivo, LocalDate.now(),empleado);
		cliente.getListaConflictos().add(nuevoRegistro);
		cliente.setEnListaNegra(true);
	}
	public void cambiarEstado(Reparacion reparacion, EstadoReparacion nuevoEstado, Empleado empleado) {
		reparacion.setEstado(nuevoEstado);
		HistorialEstado nuevoRegistro = new HistorialEstado(reparacion, nuevoEstado, empleado);
		reparacion.getHistorialEstados().add(nuevoRegistro);
	}
	public void registrarPago(Reparacion reparacion, Pago pago) {
		//TODO
	}
	
	// ESTA FUNCIÓN SE PARA EN CADA UNO DE LOS CLIENTES Y FILTRA EN UNA LISTA LAS REPARACIONES EN UN ESTADO ESPECÍFICO
	public List<Reparacion> listaReparacionPorEstado(EstadoReparacion estado){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> r.getEstado() == estado)
						.collect(Collectors.toList());
	}
	
}
