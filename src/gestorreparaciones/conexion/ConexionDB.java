package gestorreparaciones.conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConexionDB {
	
	private static final String ARCHIVO_CONFIG = "db.properties";
	
	public static Connection obtenerConexion() throws SQLException {
		Properties props = new Properties();
		
		try (FileInputStream input = new FileInputStream(ARCHIVO_CONFIG)) {
			props.load(input);
		} catch (IOException e) {
			throw new SQLException("No se pudo leer el archivo de configuración: " + ARCHIVO_CONFIG, e);
		}
		
		String url = props.getProperty("db.url");
		String usuario = props.getProperty("db.usuario");
		String password = props.getProperty("db.password");
		
		return DriverManager.getConnection(url, usuario, password);
	}
	
}
