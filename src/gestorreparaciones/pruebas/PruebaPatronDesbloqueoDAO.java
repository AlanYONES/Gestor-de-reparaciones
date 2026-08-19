package gestorreparaciones.pruebas;

import java.util.Arrays;
import java.util.List;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.PatronDesbloqueoDAO;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.sistema.Sistema;

public class PruebaPatronDesbloqueoDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP
        // =========================================================
        Reparacion reparacion = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("Ezequiel", "Roldán", TipoDocumento.DNI, "36999000",
                                           "1199001122", "ezequiel.roldan@mail.com");
            clienteDAO.guardar(cliente);

            DispositivoDAO dispositivoDAO = new DispositivoDAO();
            Dispositivo dispositivo = new Dispositivo(cliente, TipoEquipo.CELULAR, "Motorola", "Edge 30",
                                                        "356789055555555", "SN900");
            dispositivoDAO.guardar(dispositivo);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            Empleado empleado = new Empleado("Camila Ríos", "21000100200", RolEmpleado.TECNICO);
            empleadoDAO.guardar(empleado);

            reparacion = sistema.crearReparacion(dispositivo, empleado,
                    "Pantalla táctil no responde", "Golpe en esquina superior", 33000.0);
            System.out.println("Setup listo. Reparación id: " + reparacion.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. REGISTRAR PATRON (secuencia con orden significativo)
        // =========================================================
        try {
            List<Integer> patron = Arrays.asList(1, 4, 7, 8, 9);
            sistema.registrarPatronDesbloqueo(reparacion, patron);
            System.out.println("\nPatrón registrado en memoria: " + reparacion.getPatronDesbloqueo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. BUSCAR PATRON Y CONFIRMAR QUE EL ORDEN SE PRESERVÓ
        // =========================================================
        try {
            PatronDesbloqueoDAO dao = new PatronDesbloqueoDAO();
            List<Integer> recuperado = dao.buscarPorReparacion(reparacion.getId());
            System.out.println("\n--- Patrón recuperado de la base ---");
            System.out.println(recuperado);
            System.out.println("¿Coincide el orden con el original?: "
                    + recuperado.equals(Arrays.asList(1, 4, 7, 8, 9)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}