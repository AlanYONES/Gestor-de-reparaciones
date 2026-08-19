package gestorreparaciones.pruebas;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.dao.DispositivoDAO;
import gestorreparaciones.dao.EmpleadoDAO;
import gestorreparaciones.dao.PagoDAO;
import gestorreparaciones.enums.FormaDePago;
import gestorreparaciones.enums.RolEmpleado;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.enums.TipoPago;
import gestorreparaciones.excepciones.PagoInvalidoException;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.Pago;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.sistema.Sistema;

public class PruebaPagoDAO {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        // =========================================================
        // 0. SETUP: cliente, dispositivo, empleado y reparacion
        // =========================================================
        Reparacion reparacion = null;
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("Pedro", "Ramírez", TipoDocumento.DNI, "32555666",
                                           "1155667788", "pedro.ramirez@mail.com");
            clienteDAO.guardar(cliente);

            DispositivoDAO dispositivoDAO = new DispositivoDAO();
            Dispositivo dispositivo = new Dispositivo(cliente, TipoEquipo.CELULAR, "Samsung", "S21",
                                                        "356789099999999", "SN600");
            dispositivoDAO.guardar(dispositivo);

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            Empleado empleado = new Empleado("Lucía Fernández", "20600700800", RolEmpleado.TECNICO);
            empleadoDAO.guardar(empleado);

            reparacion = sistema.crearReparacion(dispositivo, empleado,
                    "Batería se agota rápido", "Sin daños visibles", 40000.0);
            System.out.println("Setup listo. Reparación id: " + reparacion.getId()
                    + " - Total servicio: " + reparacion.calculoTotalServicio());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // =========================================================
        // 1. PAGO PARCIAL EN EFECTIVO (SEÑA)
        // =========================================================
        try {
            Pago sena = new Pago(reparacion, 15000.0, FormaDePago.EFECTIVO, TipoPago.SEÑA);
            sistema.registrarPago(reparacion, sena);
            System.out.println("\nSeña registrada, id: " + sena.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 2. PAGO CON RECARGO (TARJETA)
        // =========================================================
        Pago pagoTarjeta = null;
        try {
            double recargo = sistema.sugerirRecargo(FormaDePago.TARJETA);
            pagoTarjeta = new Pago(reparacion, 25000.0, FormaDePago.TARJETA, TipoPago.SALDO);
            pagoTarjeta.setRecargoPorcentaje(recargo);
            sistema.registrarPago(reparacion, pagoTarjeta);
            System.out.println("Pago con tarjeta registrado, id: " + pagoTarjeta.getId()
                    + " - Monto con recargo (informativo): " + pagoTarjeta.montoConRecargo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 3. PAGO QUE EXCEDE EL SALDO (debe fallar)
        // =========================================================
        try {
            Pago excedido = new Pago(reparacion, 99999.0, FormaDePago.EFECTIVO, TipoPago.SALDO);
            sistema.registrarPago(reparacion, excedido);
        } catch (PagoInvalidoException e) {
            System.out.println("\nException esperada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 4. BUSCAR PAGOS POR REPARACION
        // =========================================================
        try {
            PagoDAO dao = new PagoDAO();
            System.out.println("\n--- Pagos de la reparación ---");
            dao.buscarPorReparacion(reparacion.getId()).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // =========================================================
        // 5. ANULAR UN PAGO
        // =========================================================
        try {
            sistema.anularPago(pagoTarjeta);
            System.out.println("\nPago anulado. Verificando pendiente real...");
            System.out.println("Pendiente real: " + sistema.calcularPendienteReal(reparacion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}