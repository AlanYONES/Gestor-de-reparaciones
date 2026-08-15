package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.Reparacion;

public class PatronDesbloqueoDAO {
	public void guardar(Reparacion reparacion, List<Integer> patron ) throws SQLException{
	    String sql = "INSERT INTO patron_desbloqueo (reparacion_id, posicion, orden) VALUES (?, ?, ?)";
	    try (Connection conn = ConexionDB.obtenerConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        for (int i = 0; i < patron.size(); i++) {
	            stmt.setInt(1, reparacion.getId());
	            stmt.setInt(2, patron.get(i));
	            stmt.setInt(3, i + 1);
	            stmt.addBatch();
	        }
	        stmt.executeBatch();
	    }
	}
	
	public List<Integer> buscarPorReparacion(int reparacionId) throws SQLException {
	    String sql = "SELECT posicion FROM patron_desbloqueo WHERE reparacion_id = ? ORDER BY orden";
	    List<Integer> patron = new ArrayList<>();
	    try (Connection conn = ConexionDB.obtenerConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, reparacionId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                patron.add(rs.getInt("posicion"));
	            }
	        }
	    }
	    return patron;
	}
}
