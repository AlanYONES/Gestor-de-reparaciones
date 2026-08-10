package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.PlantillaDiagnostico;

public class PlantillaDiagnosticoDAO {
	
	public void guardar(PlantillaDiagnostico plantilla) throws SQLException {
		String sql = "INSERT INTO plantilla_diagnostico (nombre, descripcion, dias_estimados) "
					+ "VALUES (?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, plantilla.getNombre());
			stmt.setString(2, plantilla.getDescripcion());
			stmt.setInt(3, plantilla.getDiasEstimados());
			
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					plantilla.setId(generatedKeys.getInt(1));
	            }
	        }
		}
	}
	
	public List<PlantillaDiagnostico> buscarTodos() throws SQLException {
		String sql = "SELECT id, nombre, descripcion, dias_estimados "
					+ "FROM plantilla_diagnostico ";
		List<PlantillaDiagnostico> plantillas = new ArrayList<>();
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					plantillas.add(mapearPlantilla(rs));
				}
			}
		}
		return plantillas;
	}
	
	public PlantillaDiagnostico mapearPlantilla(ResultSet rs) throws SQLException {
		PlantillaDiagnostico plantilla = new PlantillaDiagnostico(
										rs.getString("nombre"),
										rs.getString("descripcion"),
										rs.getInt("dias_estimados")
										);
		plantilla.setId(rs.getInt("id"));
		return plantilla;
	}
	
}
