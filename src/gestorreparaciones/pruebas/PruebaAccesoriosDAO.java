package gestorreparaciones.pruebas;

import gestorreparaciones.dao.AccesoriosDAO;
import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.FotoReparacionDAO;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.sistema.Sistema;

public class PruebaAccesoriosDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP
        // =========================================================
        Dispositivo dispositivo = null;
        Reparacion reparacion = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("Valeria", "Ponce", TipoDocumento.DNI, "35888999",
                                           "1188990011", "valeria.ponce@mail.com");
            clienteDAO.guardar(cliente);

            DispositivoDAO dispositivoDAO = new DispositivoDAO();
            dispositivo = new Dispositivo(cliente, TipoEquipo.CELULAR, "Xiaomi", "Poco X3",
                                           "356789066666666", "SN800");
            dispositivoDAO.guardar(dispositivo);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            Empleado empleado = new Empleado("Julián Paz", "20900100200", RolEmpleado.TECNICO);
            empleadoDAO.guardar(empleado);

            reparacion = sistema.crearReparacion(dispositivo, empleado,
                    "Cámara no enfoca", "Sin daños visibles", 28000.0);
            System.out.println("Setup listo. Dispositivo id: " + dispositivo.getId()
                    + " - Reparación id: " + reparacion.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. ACCESORIOS
        // =========================================================
        try {
            sistema.agregarAccesorio(dispositivo, "Cargador");
            sistema.agregarAccesorio(dispositivo, "Funda");

            AccesoriosDAO dao = new AccesoriosDAO();
            System.out.println("\n--- Accesorios del dispositivo ---");
            dao.buscarPorDispositivo(dispositivo.getId()).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}