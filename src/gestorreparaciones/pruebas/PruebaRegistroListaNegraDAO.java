package gestorreparaciones.pruebas;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.RegistroListaNegraDAO;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.sistema.Sistema;

public class PruebaRegistroListaNegraDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP
        // =========================================================
        Cliente cliente = null;
        Empleado empleado = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            cliente = new Cliente("Sergio", "Molina", TipoDocumento.DNI, "34777888",
                                   "1177889900", "sergio.molina@mail.com");
            clienteDAO.guardar(cliente);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = new Empleado("Diego Torres", "20800900100", RolEmpleado.RECEPCIONISTA);
            empleadoDAO.guardar(empleado);

            System.out.println("Setup listo. Cliente id: " + cliente.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. MARCAR EN LISTA NEGRA
        // =========================================================
        try {
            sistema.marcarListaNegra(cliente, "No retira equipos hace más de 6 meses", empleado);
            System.out.println("\n¿En lista negra?: " + cliente.isEnListaNegra());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. FILTRO DE CLIENTES EN LISTA NEGRA
        // =========================================================
        try {
            System.out.println("\n--- Clientes en lista negra ---");
            sistema.filtroClientesEnListaNegra().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 3. HISTORIAL DE REGISTROS DEL CLIENTE
        // =========================================================
        try {
            RegistroListaNegraDAO dao = new RegistroListaNegraDAO();
            System.out.println("\n--- Historial de conflictos del cliente ---");
            dao.buscarPorCliente(cliente.getId()).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 4. QUITAR DE LISTA NEGRA
        // =========================================================
        try {
            sistema.quitarDeListaNegra(cliente);
            System.out.println("\n¿En lista negra tras quitar?: " + cliente.isEnListaNegra());

            RegistroListaNegraDAO dao = new RegistroListaNegraDAO();
            System.out.println("Historial (debe seguir intacto): " + dao.buscarPorCliente(cliente.getId()).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}