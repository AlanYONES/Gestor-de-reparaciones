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
	
	//FUNCIONES RELACIONADAS A CLASE CLIENTE
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
	
	public void marcarListaNegra(Cliente cliente, String motivo,Empleado empleado) {
		RegistroListaNegra nuevoRegistro = new RegistroListaNegra(cliente, motivo, LocalDate.now(),empleado);
		cliente.getListaConflictos().add(nuevoRegistro);
		cliente.setEnListaNegra(true);
	}
	public void quitarListaNegra(Cliente cliente) {
		cliente.setEnListaNegra(false);
	}
	public List<Cliente> filtroClientesEnListaNegra(){
		return clientes.stream()
						.filter(c -> c.isEnListaNegra())
						.collect(Collectors.toList());
	}
	
	//FUNCIONES RELACIONADAS A CLASE EMPLEADO
	public void agregarEmpleado(Empleado empleado)throws EmpleadoYaExistenteException {
		for(Empleado e : empleados) {
			if(e.equals(empleado)) {
				throw new EmpleadoYaExistenteException("Ya existe un empleado con cuit: " + empleado.getCuit());
			}
		}
		empleados.add(empleado);
	}
	
	public Empleado buscarEmpleado(String cuit)throws EmpleadoNoEncontradoException {
		return empleados.stream()
							.filter(c -> c.getCuit().equals(cuit))
							.findFirst()
							.orElseThrow(() -> new EmpleadoNoEncontradoException("No se encontró empleado con cuit: " + cuit));
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
	
	//FUNCIONES DE REPARACION
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
	
	// ESTA FUNCIÓN SE PARA EN CADA UNO DE LOS CLIENTES Y FILTRA EN UNA LISTA LAS REPARACIONES EN UN ESTADO ESPECÍFICO
	public List<Reparacion> listaReparacionPorEstado(EstadoReparacion estado){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> r.getEstado() == estado)
						.collect(Collectors.toList());
	}
	
	public void cambiarEstado(Reparacion reparacion, EstadoReparacion nuevoEstado, Empleado empleado) {
		reparacion.setEstado(nuevoEstado);
		HistorialEstado nuevoRegistro = new HistorialEstado(reparacion, nuevoEstado, empleado);
		reparacion.getHistorialEstados().add(nuevoRegistro);
	}
	
	// FUNCIONES DE PAGOS
	public void registrarPago(Reparacion reparacion, Pago pago)throws PagoInvalidoException {
		if(pago.montoConRecargo() > reparacion.calcularPendiente() ) {
			throw new PagoInvalidoException("Pago invalido: supera el total de la reparacion.");
		}
		reparacion.getPagos().add(pago);
	}
	
	public double sugerirRecargo(FormaDePago pago) {
		if(pago == FormaDePago.TARJETA) {
			return 10.0;
		}
		return 0.0;
	}
	public void anularPago(Pago pago) {
		pago.setPagoInvalido();
	}
}
