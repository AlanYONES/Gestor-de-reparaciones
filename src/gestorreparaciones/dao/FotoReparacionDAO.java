package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.Reparacion;

public class FotoReparacionDAO {
	public void guardar(Reparacion reparacion, String ruta) throws SQLException{
		String sql = "INSERT INTO foto_reparacion (reparacion_id, ruta) "
					+ "VALUES (?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, reparacion.getId());
			stmt.setString(2, ruta);
			stmt.executeUpdate();
		}
	}
	
	public List<String> buscarPorReparacion(int reparacionId) throws SQLException {
		String sql = "SELECT ruta FROM foto_reparacion WHERE reparacion_id = ? ";
		List<String> rutaFotos = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, reparacionId);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					rutaFotos.add(rs.getString("ruta"));
				}
			}
		}
		return rutaFotos;
	}
}
