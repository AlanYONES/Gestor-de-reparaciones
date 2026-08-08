package gestorreparaciones.pruebas;

import java.sql.Connection;
import gestorreparaciones.conexion.ConexionDB;

public class PruebaConexion {

	public static void main(String[] args) {
		try(Connection conexion = ConexionDB.obtenerConexion()){
			System.out.println("Conexión exitosa: " + conexion);
		}catch (Exception e) {
			System.out.println("Error al conectar: " + e.getMessage());
		}

	}

}
