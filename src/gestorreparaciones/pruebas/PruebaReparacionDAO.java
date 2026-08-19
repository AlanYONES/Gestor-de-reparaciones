package gestorreparaciones.pruebas;

import java.time.LocalDate;
import java.util.List;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.ReparacionDAO;
import gestorreparaciones.enums.EstadoReparacion;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.PlantillaDiagnostico;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.sistema.Sistema;

public class PruebaReparacionDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP: cliente, dispositivo y empleado ya persistidos
        // =========================================================
        Cliente cliente = null;
        Dispositivo dispositivo = null;
        Empleado empleado = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            cliente = new Cliente("Marta", "Suárez", TipoDocumento.DNI, "31444555",
                                   "1144556677", "marta.suarez@mail.com");
            clienteDAO.guardar(cliente);

            DispositivoDAO dispositivoDAO = new DispositivoDAO();
            dispositivo = new Dispositivo(cliente, TipoEquipo.CELULAR, "Apple", "iPhone 12",
                                           "490154203237518", "SN500");
            dispositivoDAO.guardar(dispositivo);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = new Empleado("Roberto Díaz", "20500600700", RolEmpleado.TECNICO);
            empleadoDAO.guardar(empleado);

            System.out.println("Setup listo. Cliente id=" + cliente.getId()
                    + " Dispositivo id=" + dispositivo.getId() + " Empleado id=" + empleado.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. CREAR REPARACION (via Sistema)
        // =========================================================
        Reparacion reparacion = null;
        try {
            reparacion = sistema.crearReparacion(dispositivo, empleado,
                    "No enciende, pantalla en negro", "Sin golpes visibles", 45000.0);
            System.out.println("\nReparación creada, id: " + reparacion.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. APLICAR PLANTILLA
        // =========================================================
        try {
            PlantillaDiagnostico plantilla = new PlantillaDiagnostico(
                    "Diagnóstico general", "Revisión técnica completa antes de presupuestar", 2);
            sistema.aplicarPlantilla(reparacion, plantilla);
            System.out.println("Plantilla aplicada. Observaciones: " + reparacion.getObservaciones()
                    + " | Entrega estimada: " + reparacion.getFechaEntregaEstimada());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 3. BUSCAR POR ID
        // =========================================================
        try {
            ReparacionDAO dao = new ReparacionDAO();
            Reparacion encontrada = dao.buscarPorId(reparacion.getId());
            System.out.println("\n--- Buscar por id ---");
            System.out.println(encontrada);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 4. CAMBIAR ESTADO (RECIBIDO -> EN_REPARACION -> ENTREGADO)
        // =========================================================
        try {
            sistema.cambiarEstado(reparacion, EstadoReparacion.EN_REPARACION, empleado);
            System.out.println("\nEstado tras primer cambio: " + reparacion.getEstado());

            sistema.cambiarEstado(reparacion, EstadoReparacion.ENTREGADO, empleado);
            System.out.println("Estado tras segundo cambio: " + reparacion.getEstado()
                    + " | Entrega final: " + reparacion.getFechaEntregaFinal());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 5. ASIGNAR GARANTIA
        // =========================================================
        try {
            sistema.asignarGarantia(reparacion, 30);
            System.out.println("\nGarantía asignada, vence: " + reparacion.getFechaVencimientoGarantia());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 6. BUSCAR POR DISPOSITIVO / POR ESTADO / TODOS
        // =========================================================
        try {
            ReparacionDAO dao = new ReparacionDAO();

            System.out.println("\n--- Por dispositivo ---");
            dao.buscarPorDispositivo(dispositivo.getId()).forEach(System.out::println);

            System.out.println("\n--- Por estado ENTREGADO ---");
            dao.buscarPorEstado(EstadoReparacion.ENTREGADO).forEach(System.out::println);

            System.out.println("\n--- Todos ---");
            dao.buscarTodos().forEach(r -> System.out.println("Orden #" + r.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 7. BUSCAR POR RANGO DE FECHA
        // =========================================================
        try {
            ReparacionDAO dao = new ReparacionDAO();
            List<Reparacion> enRango = dao.buscarPorRangoFecha(
                    LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
            System.out.println("\n--- Por rango de fecha (ayer a mañana) ---");
            System.out.println("Encontradas: " + enRango.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 8. CANCELAR REPARACION (con y sin cargo) - sobre reparaciones nuevas
        // =========================================================
        try {
            Reparacion sinCargo = sistema.crearReparacion(dispositivo, empleado,
                    "No tiene arreglo", "Placa quemada", 20000.0);
            sistema.cancelarReparacion(sinCargo, false, 0.0);
            System.out.println("\nCancelada sin cargo. Total: " + sinCargo.calculoTotalServicio());

            Reparacion conCargo = sistema.crearReparacion(dispositivo, empleado,
                    "Cliente no quiere reparar", "N/A", 30000.0);
            sistema.cancelarReparacion(conCargo, true, 2500.0);
            System.out.println("Cancelada con cargo. Total: " + conCargo.calculoTotalServicio());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}