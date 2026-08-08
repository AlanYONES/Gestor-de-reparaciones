package gestorreparaciones.pruebas;

import java.util.List;
import java.util.ArrayList;

import gestorreparaciones.dao.ClienteDAO;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.modelo.Cliente;

public class PruebaClienteDAO {

	public static void main(String[] args) {
		try {
			Cliente cliente = new Cliente ("Juan", "Pérez", TipoDocumento.DNI, "30111222", "1122334455", "juan@mail.com");
            System.out.println("Antes de guardar, id: " + cliente.getId());
            
            ClienteDAO dao = new ClienteDAO();
            dao.guardar(cliente);
            
            System.out.println("Despues de guardar, id: " + cliente.getId());
            
			System.out.println("Busqueda cliente de id 2 : ");
			ClienteDAO daoBusqueda = new ClienteDAO();
			Cliente clienteBusqueda = daoBusqueda.buscarPorId(2);
			if(clienteBusqueda != null) {
				System.out.println("Encontrado: " + clienteBusqueda);
			}else {
               System.out.println("No se encontró cliente con ese id");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			ClienteDAO dao = new ClienteDAO();
			System.out.println(dao.buscarTodos());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			ClienteDAO dao = new ClienteDAO();
			Cliente clienteModificado = dao.buscarPorId(2);
			System.out.println("Cliente antes de modificacion: " + dao.buscarPorId(2));
			clienteModificado.setNombre("Alan");
			clienteModificado.setApellido("Yones");
			dao.actualizar(clienteModificado);
			System.out.println("Cliente luego de modificacion: " + dao.buscarPorId(2));
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
