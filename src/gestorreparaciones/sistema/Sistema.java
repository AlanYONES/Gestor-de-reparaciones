package gestorreparaciones.sistema;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import gestorreparaciones.modelo.*;
import gestorreparaciones.enums.*;
import gestorreparaciones.excepciones.*;

public class Sistema {
	private List<Cliente> clientes;
	private List<Empleado> empleados;
	private List<PlantillaDiagnostico> plantillasDiagnostico;
	
	public Sistema() {
		this.clientes = new ArrayList<>();
		this.empleados = new ArrayList<>();
		this.plantillasDiagnostico = new ArrayList<>();
	}
	
	public List<Cliente> getClientes(){
		return new ArrayList<>(clientes);
	}
	public List<Empleado> getEmpleados(){
		return new ArrayList<>(empleados);
	}
	public List<PlantillaDiagnostico> getPlantillas(){
		return new ArrayList<>(plantillasDiagnostico);
	}
	
	
	public void imprimirLista(List<?> lista) {
	    lista.forEach(System.out::println);
	}
	
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
	public void quitarDeListaNegra(Cliente cliente) {
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
	public void darBajaEmpleado(Empleado empleado) {
		empleado.setActivo(false);
	}
	public void darAltaEmpleado(Empleado empleado) {
		empleado.setActivo(true);
	}
	
	public Empleado empleadoConMasReparaciones() {
		Map<Empleado, Long> conteoPorEmpleado = clientes.stream()
															.flatMap(c -> c.getDispositivos().stream())
															.flatMap(d -> d.getReparaciones().stream())
															.collect(Collectors.groupingBy(Reparacion::getEmpleado, Collectors.counting()));
		return conteoPorEmpleado.entrySet().stream()
											.max(Map.Entry.comparingByValue())
											.map(Map.Entry::getKey)
											.orElse(null);
	}
	
	public List<Empleado> ListadoEmpleadosPorReparacionesAscendente(){
		Map<Empleado, Long> conteoPorEmpleado = clientes.stream()
														.flatMap(c -> c.getDispositivos().stream())
														.flatMap(d -> d.getReparaciones().stream())
														.collect(Collectors.groupingBy(Reparacion::getEmpleado, Collectors.counting()));
		return conteoPorEmpleado.entrySet().stream()
											.sorted(Map.Entry.comparingByValue())
											.map(Map.Entry::getKey)
											.toList();
	}
	
	public List<Empleado> ListadoEmpleadosPorReparacionesDescendente(){
		Map<Empleado, Long> conteoPorEmpleado = clientes.stream()
														.flatMap(c -> c.getDispositivos().stream())
														.flatMap(d -> d.getReparaciones().stream())
														.collect(Collectors.groupingBy(Reparacion::getEmpleado, Collectors.counting()));
		return conteoPorEmpleado.entrySet().stream()
											.sorted(Map.Entry.<Empleado, Long>comparingByValue().reversed())
											.map(Map.Entry::getKey)
											.toList();
	}
	
	
	
	// FUNCIONES DE DISPOSITIVOS
	public void agregarDispositivo(Cliente cliente, Dispositivo dispositivo)throws DispositivoDuplicadoException {
		boolean duplicado = cliente.getDispositivos().stream()
														.anyMatch(c -> dispositivo.getImei() != null
																	&& dispositivo.getImei().equals(c.getImei()));
		if(duplicado) {
			throw new DispositivoDuplicadoException("Ya existe un dispositivo con imei: " + dispositivo.getImei());
		}	
		cliente.getDispositivos().add(dispositivo);
	}
	
	public List<Dispositivo> buscarDispositivoPorModelo(String modelo) {
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.filter(d -> d.getModelo().equalsIgnoreCase(modelo))
						.collect(Collectors.toList());
	}
	public Dispositivo buscarDispositivoPorImei(String imei)throws DispositivoNoEncontradoException {
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.filter(d ->d.getImei() != null && d.getImei().equals(imei))
						.findFirst()
						.orElseThrow(() -> new DispositivoNoEncontradoException("No se encontró dispositivo con imei: " + imei));
	}
	
	public void agregarAccesorio(Dispositivo dispositivo, String accesorio) {
		dispositivo.getAccesorios().add(accesorio);
	}
	
	
	
	
	//FUNCIONES DE REPARACION
	public Reparacion crearReparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
										String estadoFisicoAlRecibir, double presupuesto)throws EmpleadoInactivoException {
		if(!empleado.isActivo()) {
			throw new EmpleadoInactivoException(" El empleado " + empleado.getNombre() + " no está activo y no puede recibir reparaciones.");
		}
		Reparacion retorno = new Reparacion(dispositivo, empleado, fallaDeclarada, 
												estadoFisicoAlRecibir , presupuesto);
		dispositivo.getReparaciones().add(retorno);
		return retorno;
	}
	
	//CAMBIA ESTADO DE REPARACION A CANCELADA, SI SE COBRA REVISION SE AGREGA EL MONTO PARA SUMARLO
	public void cancelarReparacion(Reparacion reparacion, boolean conCargo, double cargoRevision) {
		reparacion.setEstado(EstadoReparacion.CANCELADA);
		reparacion.setCanceladaConCargo(conCargo);
		if(conCargo) {
			reparacion.setCargoRevision(cargoRevision);
		}
	}
	// ESTA FUNCIÓN SE PARA EN CADA UNO DE LOS CLIENTES Y FILTRA EN UNA LISTA LAS REPARACIONES EN UN ESTADO ESPECÍFICO
	public List<Reparacion> listaReparacionPorEstado(EstadoReparacion estado){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> r.getEstado() == estado)
						.collect(Collectors.toList());
	}
	public Map<EstadoReparacion, Long> cantidadReparacionesPorEstado(){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.collect(Collectors.groupingBy(Reparacion::getEstado, Collectors.counting()));
	}
	
	public Reparacion buscarReparacionPorId(int id) throws ReparacionNoEncontradaException{
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> r.getId() == id)
						.findFirst()
						.orElseThrow(() -> new ReparacionNoEncontradaException("No se encontro la reparacion con orden n°: " + id));
	}
	
	public void cambiarEstado(Reparacion reparacion, EstadoReparacion nuevoEstado, Empleado empleado) {
		reparacion.setEstado(nuevoEstado);
		
		if(nuevoEstado == EstadoReparacion.ENTREGADO) {
			reparacion.setFechaEntregaFinal(LocalDateTime.now());
		}
		
		HistorialEstado nuevoRegistro = new HistorialEstado(reparacion, nuevoEstado, empleado);
		reparacion.getHistorialEstados().add(nuevoRegistro);
	}
	
	public void asignarGarantia(Reparacion reparacion, int dias) {
		if (reparacion.getFechaEntregaFinal() == null) {
			throw new IllegalStateException("No se puede asignar garantia a una reparacion que no fué entregada");
		}
		reparacion.setTieneGarantia(true);
		reparacion.setDiasGarantia(dias);
		reparacion.setFechaVencimientoGarantia(reparacion.getFechaEntregaFinal().toLocalDate().plusDays(dias));
	}
	
	public void aplicarPlantilla(Reparacion reparacion, PlantillaDiagnostico plantilla) {
		reparacion.setObservaciones(plantilla.getDescripcion());
		reparacion.setFechaEntregaEstimada(LocalDate.now().plusDays(plantilla.getDiasEstimados()));
	}
	public void agregarRutaFoto(Reparacion reparacion, String ruta ) {
		reparacion.getRutasFotos().add(ruta);
	}
	
	public List<GarantiaInfo> dispositivosEnGarantia(){
		return clientes.stream()
                  .flatMap(c -> c.getDispositivos().stream())
                  .filter(d -> d.getReparaciones().stream()
                                .anyMatch(r -> r.isTieneGarantia() 
                                           && r.getFechaVencimientoGarantia().isAfter(LocalDate.now())))
                  .map(d -> {
                      long dias = d.getReparaciones().stream()
                                    .filter(r -> r.isTieneGarantia() 
                                               && r.getFechaVencimientoGarantia().isAfter(LocalDate.now()))
                                    .map(r -> ChronoUnit.DAYS.between(LocalDate.now(), r.getFechaVencimientoGarantia()))
                                    .max(Long::compareTo)
                                    .orElse(0L);
                      return new GarantiaInfo(d, dias);
                  })
                  .sorted(Comparator.comparingLong(GarantiaInfo::getDiasRestantes))
                  .toList();  		
	}
	
	public GarantiaInfo verificacionGarantia(Dispositivo dispositivo) {
		return dispositivo.getReparaciones().stream()
											.filter(r -> r.isTieneGarantia() && r.getFechaVencimientoGarantia().isAfter(LocalDate.now()))
											.map(r -> new GarantiaInfo(dispositivo, ChronoUnit.DAYS.between(LocalDate.now()	, r.getFechaVencimientoGarantia())))
											.max(Comparator.comparingLong(GarantiaInfo::getDiasRestantes))
											.orElse(null);
	}
	
	// FUNCIONES DE PAGOS
	public void registrarPago(Reparacion reparacion, Pago pago)throws PagoInvalidoException {
		if(pago.getMonto() > reparacion.calcularPendiente() ) {
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
	public List<Pago> buscarPagosPorReparacion(Reparacion reparacion){
		return reparacion.getPagos();
	}
	
	
	
	// FUNCIONES DE REPORTE
	public double totalRecaudadoPorRangoFecha(LocalDate desde, LocalDate hasta){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> !r.getFechaEntrada().toLocalDate().isBefore(desde)
									&& !r.getFechaEntrada().toLocalDate().isAfter(hasta))
						.mapToDouble(Reparacion::calculoTotalServicio)
						.sum();
	}
	
	public List<Reparacion> reparacionesVencidas(){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.filter(r -> r.getEstado() != EstadoReparacion.ENTREGADO
									&& r.getEstado() != EstadoReparacion.CANCELADA)
						.filter(r -> r.getFechaEntregaEstimada().isBefore(LocalDate.now()))
						.toList();
	}
	
	
}
