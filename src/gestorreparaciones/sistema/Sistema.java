package gestorreparaciones.sistema;

import java.util.List;
import java.util.stream.Collectors;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import gestorreparaciones.modelo.*;
import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.ReparacionDAO;
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
	
	public List<Cliente> getClientes() throws SQLException{
		ClienteDAO dao = new ClienteDAO();
		return dao.buscarTodos();
	}
	public List<Empleado> getEmpleados() throws SQLException{
		EmpleadoDAO dao = new EmpleadoDAO();
		return dao.buscarTodos();
	}
	public List<PlantillaDiagnostico> getPlantillas(){
		return new ArrayList<>(plantillasDiagnostico);
	}
	
	
	public void imprimirLista(List<?> lista) {
	    lista.forEach(System.out::println);
	}
	
	//FUNCIONES RELACIONADAS A CLASE CLIENTE
	public void agregarCliente(Cliente cliente)throws ClienteYaExistenteException, SQLException{
		ClienteDAO dao = new ClienteDAO();
		
			if(dao.existePorDocumento(cliente.getNumeroDocumento())) {
				throw new ClienteYaExistenteException("Ya existe un cliente con documento " + cliente.getNumeroDocumento(), " Documento duplicado.");
			}
		dao.guardar(cliente);
	}
	
	
	public Cliente buscarCliente(String dni)throws ClienteNoEncontradoException, SQLException {
		ClienteDAO dao = new ClienteDAO();
		Cliente cliente = dao.buscarPorDocumento(dni);
		if(cliente != null) {
			return cliente;
		}
		throw new ClienteNoEncontradoException("No se encuentra cliente con documento: " + dni);
	}
	
	public void marcarListaNegra(Cliente cliente, String motivo,Empleado empleado) {
		RegistroListaNegra nuevoRegistro = new RegistroListaNegra(cliente, motivo, LocalDate.now(),empleado);
		cliente.getListaConflictos().add(nuevoRegistro);
		cliente.setEnListaNegra(true);
	}
	public void quitarDeListaNegra(Cliente cliente) throws SQLException {
		cliente.setEnListaNegra(false);
		ClienteDAO dao = new ClienteDAO();
		dao.quitarListaNegra(cliente);
	}
	public List<Cliente> filtroClientesEnListaNegra() throws SQLException{
		ClienteDAO dao = new ClienteDAO();
		return dao.filtroPorListaNegra();
	}
	
	
	
	
	//FUNCIONES RELACIONADAS A CLASE EMPLEADO
	public void agregarEmpleado(Empleado empleado)throws EmpleadoYaExistenteException, SQLException {
		EmpleadoDAO dao = new EmpleadoDAO();
		
		if(dao.existePorCuit(empleado.getCuit())) {
			throw new EmpleadoYaExistenteException("Ya existe empleado con el cuit: " + empleado.getCuit());
		}
		dao.guardar(empleado);
	}
	
	public Empleado buscarEmpleado(String cuit)throws EmpleadoNoEncontradoException, SQLException {
		EmpleadoDAO dao = new EmpleadoDAO();
		Empleado empleado = dao.buscarPorCuit(cuit);
		if(empleado != null) return empleado;
		throw new EmpleadoNoEncontradoException ("No se encuentra empleado con cuit : " + cuit);
	}
	public void darBajaEmpleado(Empleado empleado) throws SQLException {
		empleado.setActivo(false);
		EmpleadoDAO dao = new EmpleadoDAO();
		dao.cambiarActivo(empleado.getId(), false);
	}
	public void darAltaEmpleado(Empleado empleado) throws SQLException {
		empleado.setActivo(true);
		EmpleadoDAO dao = new EmpleadoDAO();
		dao.cambiarActivo(empleado.getId(), true);
	}
	
	
	// LAS FUNCIONES RELACIONADAS A REPARACIONES DE EMPLEADO ESPERAN A MUDA A DAO DE CLASE REPARACIONES
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
	
	public List<Empleado> listadoEmpleadosPorReparacionesAscendente(){
		Map<Empleado, Long> conteoPorEmpleado = clientes.stream()
														.flatMap(c -> c.getDispositivos().stream())
														.flatMap(d -> d.getReparaciones().stream())
														.collect(Collectors.groupingBy(Reparacion::getEmpleado, Collectors.counting()));
		return conteoPorEmpleado.entrySet().stream()
											.sorted(Map.Entry.comparingByValue())
											.map(Map.Entry::getKey)
											.toList();
	}
	
	public List<Empleado> listadoEmpleadosPorReparacionesDescendente(){
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
	public void agregarDispositivo(Dispositivo dispositivo)throws DispositivoDuplicadoException, SQLException{
		DispositivoDAO dao = new DispositivoDAO();
		
		if(dispositivo.getImei() != null && dao.existePorImei(dispositivo.getImei())) {
			throw new DispositivoDuplicadoException("Ya existe un dispositivo con imei: " + dispositivo.getImei());
		}	
		dao.guardar(dispositivo);
	}
	
	public List<Dispositivo> buscarDispositivoPorModelo(String modelo) throws SQLException{
		DispositivoDAO dao = new DispositivoDAO();
		return dao.buscarPorModelo(modelo);
	}
	public Dispositivo buscarDispositivoPorImei(String imei)throws DispositivoNoEncontradoException, SQLException {
		DispositivoDAO dao = new DispositivoDAO();
		Dispositivo dispositivo = dao.buscarPorImei(imei);
		if(dispositivo == null) {
			throw new DispositivoNoEncontradoException("No se encontró dispositivo con imei: " + imei);
		}
		return dispositivo;
	}
	
	public void agregarAccesorio(Dispositivo dispositivo, String accesorio) {
		dispositivo.getAccesorios().add(accesorio);
	}
	
	
	
	
	//FUNCIONES DE REPARACION
	public Reparacion crearReparacion(Dispositivo dispositivo, Empleado empleado, String fallaDeclarada,
										String estadoFisicoAlRecibir, double presupuesto)throws EmpleadoInactivoException, SQLException {
		if(!empleado.isActivo()) {
			throw new EmpleadoInactivoException(" El empleado " + empleado.getNombre() + " no está activo y no puede recibir reparaciones.");
		}
		Reparacion retorno = new Reparacion(dispositivo, empleado, fallaDeclarada, 
												estadoFisicoAlRecibir , presupuesto);
		ReparacionDAO dao = new ReparacionDAO();
		dao.guardar(retorno);
		return retorno;
	}
	
	//CAMBIA ESTADO DE REPARACION A CANCELADA, SI SE COBRA REVISION SE AGREGA EL MONTO PARA SUMARLO
	public void cancelarReparacion(Reparacion reparacion, boolean conCargo, double cargoRevision)throws SQLException {
		reparacion.setEstado(EstadoReparacion.CANCELADA);
		reparacion.setCanceladaConCargo(conCargo);
		if(conCargo) {
			reparacion.setCargoRevision(cargoRevision);
		}
		ReparacionDAO dao = new ReparacionDAO();
		dao.actualizarCancelacion(reparacion);
	}
	// ESTA FUNCIÓN SE PARA EN CADA UNO DE LOS CLIENTES Y FILTRA EN UNA LISTA LAS REPARACIONES EN UN ESTADO ESPECÍFICO
	public List<Reparacion> listaReparacionPorEstado(EstadoReparacion estado)throws SQLException{
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarPorEstado(estado);
	}
	public Map<EstadoReparacion, Long> cantidadReparacionesPorEstado(){
		return clientes.stream()
						.flatMap(c -> c.getDispositivos().stream())
						.flatMap(d -> d.getReparaciones().stream())
						.collect(Collectors.groupingBy(Reparacion::getEstado, Collectors.counting()));
	}
	
	public List<Reparacion> listaReparacionesPorCliente(String dni) throws ClienteNoEncontradoException, SQLException{
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarPorDocumento(dni);
	}
	
	public Reparacion buscarReparacionPorId(int id) throws ReparacionNoEncontradaException, SQLException{
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarPorId(id); 
	}
	
	public void cambiarEstado(Reparacion reparacion, EstadoReparacion nuevoEstado, Empleado empleado) throws SQLException {
		reparacion.setEstado(nuevoEstado);
		
		if(nuevoEstado == EstadoReparacion.ENTREGADO) {
			reparacion.setFechaEntregaFinal(LocalDateTime.now());
		}
		
		HistorialEstado nuevoRegistro = new HistorialEstado(reparacion, nuevoEstado, empleado);
		reparacion.getHistorialEstados().add(nuevoRegistro);
		ReparacionDAO dao = new ReparacionDAO();
		dao.actualizarEstado(reparacion);
	}
	
	public void asignarGarantia(Reparacion reparacion, int dias)throws SQLException {
		if (reparacion.getFechaEntregaFinal() == null) {
			throw new IllegalStateException("No se puede asignar garantia a una reparacion que no fué entregada");
		}
		reparacion.setTieneGarantia(true);
		reparacion.setDiasGarantia(dias);
		reparacion.setFechaVencimientoGarantia(reparacion.getFechaEntregaFinal().toLocalDate().plusDays(dias));
		ReparacionDAO dao = new ReparacionDAO();
		dao.actualizarGarantia(reparacion);
	}
	
	public void asignarEntrega(Reparacion reparacion, Empleado empleado, boolean tieneGarantia, int diasGarantia) throws SQLException{
		cambiarEstado(reparacion, EstadoReparacion.ENTREGADO, empleado);
		if(tieneGarantia) {
			asignarGarantia(reparacion, diasGarantia);
		}
		ReparacionDAO dao = new ReparacionDAO();
		dao.actualizarEstado(reparacion);
		dao.actualizarGarantia(reparacion);
	}
	
	public void aplicarPlantilla(Reparacion reparacion, PlantillaDiagnostico plantilla)throws SQLException {
		reparacion.setObservaciones(plantilla.getDescripcion());
		reparacion.setFechaEntregaEstimada(LocalDate.now().plusDays(plantilla.getDiasEstimados()));
		ReparacionDAO dao = new ReparacionDAO();
		dao.actualizarObservacionesYFecha(reparacion);
	}
	//========================= PENDIENTE ================================
	public void agregarRutaFoto(Reparacion reparacion, String ruta ) {
		reparacion.getRutasFotos().add(ruta);
	}
	//====================================================================
	public List<GarantiaInfo> dispositivosEnGarantia() throws SQLException {
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarConGarantia();
	}
	
	public List<GarantiaInfo> verificacionGarantia(Dispositivo dispositivo) throws SQLException{
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarConGarantia(dispositivo);
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
		pago.setAnularPago();
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
	
	public List<Reparacion> reparacionesVencidas()throws SQLException{
		ReparacionDAO dao = new ReparacionDAO();
		return dao.buscarVencidas();
	}
	
	
}
