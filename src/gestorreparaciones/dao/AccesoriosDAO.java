package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.Dispositivo;


public class AccesoriosDAO {
	public void guardar(Dispositivo dispositivo, String descripcion) throws SQLException{
		String sql = "INSERT INTO accesorios (dispositivo_id, descripcion) "
				+ "VALUES (?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, dispositivo.getId());
			stmt.setString(2, descripcion);
			stmt.executeUpdate();
		}
	}
	
	public List<String> buscarPorDispositivo(int dispositivoId) throws SQLException{
		String sql = "SELECT descripcion FROM accesorios WHERE dispositivo_id = ?";
		List<String> accesorios = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, dispositivoId);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					accesorios.add(rs.getString("descripcion"));
				}
			}
		}
		return accesorios;
	}
	
}
