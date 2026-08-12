package gestorreparaciones.pruebas;

import java.util.List;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.excepciones.DispositivoDuplicadoException;
import gestorreparaciones.excepciones.DispositivoNoEncontradoException;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.sistema.Sistema;
 
public class PruebaDispositivoDAO {
 
    public static void main(String[] args) {
 
        Sistema sistema = new Sistema();
 
        // =========================================================
        // 0. SETUP: cliente ya persistido (requisito para dispositivos)
        // =========================================================
        Cliente cliente = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            cliente = new Cliente("Carlos", "Gómez", TipoDocumento.DNI, "30222333",
                                   "1133445566", "carlos.gomez@mail.com");
            clienteDAO.guardar(cliente);
            System.out.println("Cliente persistido, id: " + cliente.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
 
        // =========================================================
        // 1. GUARDAR (con IMEI)
        // =========================================================
        System.out.println("\n--- Guardar dispositivo con IMEI ---");
        Dispositivo dispositivo1 = null;
        try {
            DispositivoDAO dao = new DispositivoDAO();
            dispositivo1 = new Dispositivo(cliente, TipoEquipo.CELULAR, "Samsung", "A22 5G",
                                            "356789012345678", "SN001");
            dao.guardar(dispositivo1);
            System.out.println("Guardado, id: " + dispositivo1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 2. GUARDAR (sin IMEI, caso realista)
        // =========================================================
        System.out.println("\n--- Guardar dispositivo sin IMEI ---");
        Dispositivo dispositivo2 = null;
        try {
            DispositivoDAO dao = new DispositivoDAO();
            dispositivo2 = new Dispositivo(cliente, TipoEquipo.CELULAR, "Motorola", "G32",
                                            null, "SN002");
            dao.guardar(dispositivo2);
            System.out.println("Guardado, id: " + dispositivo2.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 3. BUSCAR POR ID
        // =========================================================
        System.out.println("\n--- Buscar por id ---");
        try {
            DispositivoDAO dao = new DispositivoDAO();
            Dispositivo encontrado = dao.buscarPorId(dispositivo1.getId());
            System.out.println("Encontrado: " + encontrado);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 4. BUSCAR TODOS
        // =========================================================
        System.out.println("\n--- Buscar todos ---");
        try {
            DispositivoDAO dao = new DispositivoDAO();
            List<Dispositivo> todos = dao.buscarTodos();
            todos.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 5. BUSCAR POR CLIENTE
        // =========================================================
        System.out.println("\n--- Buscar por cliente ---");
        try {
            DispositivoDAO dao = new DispositivoDAO();
            List<Dispositivo> delCliente = dao.buscarPorCliente(cliente.getId());
            System.out.println("Dispositivos de " + cliente.getNombre() + ":");
            delCliente.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 6. BUSCAR POR MODELO (via Sistema)
        // =========================================================
        System.out.println("\n--- Buscar por modelo via Sistema ---");
        try {
            List<Dispositivo> porModelo = sistema.buscarDispositivoPorModelo("A22 5G");
            porModelo.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 7. BUSCAR POR IMEI (via Sistema) - existente y no encontrado
        // =========================================================
        System.out.println("\n--- Buscar por IMEI via Sistema ---");
        try {
            System.out.println(sistema.buscarDispositivoPorImei("356789012345678"));
        } catch (Exception e) {
            System.out.println("Exception inesperada: " + e.getMessage());
        }
 
        try {
            sistema.buscarDispositivoPorImei("000000000000000");
        } catch (DispositivoNoEncontradoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 8. AGREGAR DISPOSITIVO VIA SISTEMA (con validación de IMEI duplicado)
        // =========================================================
        System.out.println("\n--- Agregar dispositivo via Sistema ---");
        try {
            Dispositivo nuevo = new Dispositivo(cliente, TipoEquipo.TABLET, "Lenovo", "Tab M10",
                                                 "111222333444555", "SN003");
            sistema.agregarDispositivo(nuevo);
            System.out.println("Agregado correctamente: " + nuevo);
 
            // Caso negativo: IMEI duplicado
            Dispositivo duplicado = new Dispositivo(cliente, TipoEquipo.CELULAR, "Otro", "Modelo",
                                                      "356789012345678", "SN999");
            sistema.agregarDispositivo(duplicado);
 
        } catch (DispositivoDuplicadoException e) {
            System.out.println("Exception esperada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // =========================================================
        // 9. AGREGAR DISPOSITIVO SIN IMEI VIA SISTEMA (no debe bloquear nunca)
        // =========================================================
        System.out.println("\n--- Agregar dos dispositivos sin IMEI (no deben chocar entre si) ---");
        try {
            Dispositivo sinImei1 = new Dispositivo(cliente, TipoEquipo.CELULAR, "Xiaomi", "Redmi 9",
                                                     null, "SN004");
            sistema.agregarDispositivo(sinImei1);
            System.out.println("Agregado 1: " + sinImei1);
 
            Dispositivo sinImei2 = new Dispositivo(cliente, TipoEquipo.CELULAR, "Xiaomi", "Redmi 10",
                                                     null, "SN005");
            sistema.agregarDispositivo(sinImei2);
            System.out.println("Agregado 2: " + sinImei2);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
