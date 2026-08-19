package gestorreparaciones.pruebas;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.HistorialEstadoDAO;
import gestorreparaciones.enums.EstadoReparacion;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.sistema.Sistema;

public class PruebaHistorialEstadoDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP
        // =========================================================
        Reparacion reparacion = null;
        Empleado empleado = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("Nora", "Vega", TipoDocumento.DNI, "33666777",
                                           "1166778899", "nora.vega@mail.com");
            clienteDAO.guardar(cliente);

            DispositivoDAO dispositivoDAO = new DispositivoDAO();
            Dispositivo dispositivo = new Dispositivo(cliente, TipoEquipo.TABLET, "Lenovo", "Tab P11",
                                                        "356789077777777", "SN700");
            dispositivoDAO.guardar(dispositivo);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = new Empleado("Marcos Ibáñez", "20700800900", RolEmpleado.TECNICO);
            empleadoDAO.guardar(empleado);

            reparacion = sistema.crearReparacion(dispositivo, empleado,
                    "Se traba constantemente", "Sin daños físicos", 35000.0);
            System.out.println("Setup listo. Reparación id: " + reparacion.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. CAMBIOS DE ESTADO (generan historial automáticamente)
        // =========================================================
        try {
            sistema.cambiarEstado(reparacion, EstadoReparacion.EN_REPARACION, empleado);
            sistema.cambiarEstado(reparacion, EstadoReparacion.ENTREGADO, empleado);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. BUSCAR HISTORIAL COMPLETO
        // =========================================================
        try {
            HistorialEstadoDAO dao = new HistorialEstadoDAO();
            System.out.println("\n--- Historial de estados ---");
            dao.buscarPorReparacion(reparacion.getId()).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}