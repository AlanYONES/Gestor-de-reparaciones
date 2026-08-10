package gestorreparaciones.pruebas;

import java.util.List;

import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.excepciones.EmpleadoNoEncontradoException;
import gestorreparaciones.excepciones.EmpleadoYaExistenteException;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.sistema.Sistema;

public class PruebaEmpleadoDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 1. GUARDAR (alta directa via DAO)
        // =========================================================
        System.out.println("--- Guardar empleado ---");
        Empleado empleado1 = new Empleado("Juan Pérez", "20304050607", RolEmpleado.TECNICO);
        System.out.println("Antes de guardar, id: " + empleado1.getId());

        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            dao.guardar(empleado1);
            System.out.println("Después de guardar, id: " + empleado1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. BUSCAR POR ID
        // =========================================================
        System.out.println("\n--- Buscar por id ---");
        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            Empleado encontrado = dao.buscarPorId(empleado1.getId());
            System.out.println("Encontrado: " + encontrado);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 3. BUSCAR POR CUIT
        // =========================================================
        System.out.println("\n--- Buscar por cuit ---");
        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            Empleado encontrado = dao.buscarPorCuit("20304050607");
            System.out.println("Encontrado: " + encontrado);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 4. EXISTE POR CUIT
        // =========================================================
        System.out.println("\n--- Existe por cuit ---");
        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            System.out.println("¿Existe 20304050607?: " + dao.existePorCuit("20304050607"));
            System.out.println("¿Existe 00000000000?: " + dao.existePorCuit("00000000000"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 5. BUSCAR TODOS
        // =========================================================
        System.out.println("\n--- Buscar todos ---");
        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            List<Empleado> todos = dao.buscarTodos();
            todos.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 6. AGREGAR EMPLEADO VIA SISTEMA (con validación de duplicado)
        // =========================================================
        System.out.println("\n--- Agregar empleado via Sistema ---");
        try {
            Empleado empleado2 = new Empleado("Ana García", "20405060708", RolEmpleado.RECEPCIONISTA);
            sistema.agregarEmpleado(empleado2);
            System.out.println("Empleado agregado: " + empleado2);

            // Caso negativo: cuit duplicado
            Empleado duplicado = new Empleado("Otro Nombre", "20304050607", RolEmpleado.TECNICO);
            sistema.agregarEmpleado(duplicado);

        } catch (EmpleadoYaExistenteException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 7. BUSCAR EMPLEADO VIA SISTEMA (existente y no encontrado)
        // =========================================================
        System.out.println("\n--- Buscar empleado via Sistema ---");
        try {
            System.out.println(sistema.buscarEmpleado("20304050607"));
        } catch (Exception e) {
            System.out.println("Exception inesperada: " + e.getMessage());
        }

        try {
            sistema.buscarEmpleado("99999999999");
        } catch (EmpleadoNoEncontradoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 8. DAR BAJA / DAR ALTA
        // =========================================================
        System.out.println("\n--- Dar baja / dar alta ---");
        try {
            System.out.println("Activo antes de la baja: " + empleado1.isActivo());
            sistema.darBajaEmpleado(empleado1);
            System.out.println("Activo después de la baja: " + empleado1.isActivo());

            EmpleadoDAO dao = new EmpleadoDAO();
            Empleado verificacion = dao.buscarPorId(empleado1.getId());
            System.out.println("Confirmado en base tras baja: " + verificacion.isActivo());

            sistema.darAltaEmpleado(empleado1);
            System.out.println("Activo después de dar de alta de nuevo: " + empleado1.isActivo());

            verificacion = dao.buscarPorId(empleado1.getId());
            System.out.println("Confirmado en base tras alta: " + verificacion.isActivo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
