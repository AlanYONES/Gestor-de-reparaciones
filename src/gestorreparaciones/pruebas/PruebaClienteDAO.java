package gestorreparaciones.pruebas;

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
			Cliente clienteBusqueda = daoBusqueda.buscarPorId(8);
			if(clienteBusqueda != null) {
				System.out.println("Encontrado: " + clienteBusqueda);
			}else {
               System.out.println("No se encontró cliente con ese id");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
