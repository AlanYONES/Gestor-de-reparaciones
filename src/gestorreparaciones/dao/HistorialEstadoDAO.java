package gestorreparaciones.dao;

import gestorreparaciones.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.modelo.HistorialEstado;
import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.modelo.Empleado;

import gestorreparaciones.enums.EstadoReparacion;

public class HistorialEstadoDAO {
	
	public void guardar(HistorialEstado historial) throws SQLException{
		String sql = "INSERT into historial_estados (reparacion_id, estado_reparacion, fecha, empleado_id) "
					+ "VALUES (?, ?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, historial.getReparacion().getId());
			stmt.setString(2, historial.getEstado().name());
			stmt.setTimestamp(3, Timestamp.valueOf(historial.getFecha()));
			stmt.setInt(4, historial.getEmpleado().getId());
			
			stmt.executeUpdate();
			try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if(generatedKeys.next()) {
					historial.setId(generatedKeys.getInt(1));
				}
			}
		}
	}
	
	public HistorialEstado buscarPorId(int id) throws SQLException{
		String sql = "SELECT id, reparacion_id, estado_reparacion, fecha, empleado_id "
				+ "FROM historial_estados "
				+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearHistorialEstado(rs);
				}
			}
		}
		return null;
	}
	
	public List<HistorialEstado> buscarPorReparacion(int reparacionId) throws SQLException{
		String sql = "SELECT id, reparacion_id, estado_reparacion, fecha, empleado_id "
					+ "FROM historial_estados "
					+ "WHERE reparacion_id = ?";
		List<HistorialEstado> historial = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, reparacionId);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					historial.add(mapearHistorialEstado(rs));
				}
			}
		}
		return historial;
	}
	
	public HistorialEstado mapearHistorialEstado(ResultSet rs) throws SQLException{
		int empleadoId = rs.getInt("empleado_id");
		int reparacionId = rs.getInt("reparacion_id");
		
		EmpleadoDAO daoEmpleado = new EmpleadoDAO();
		Empleado empleado = daoEmpleado.buscarPorId(empleadoId);
		ReparacionDAO daoReparacion = new ReparacionDAO();
		Reparacion reparacion = daoReparacion.buscarPorId(reparacionId);
		
		HistorialEstado historial = new HistorialEstado(
				reparacion,
				EstadoReparacion.valueOf(rs.getString("estado_reparacion")),
				empleado
				);
		historial.setId(rs.getInt("id"));
		historial.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
		
		
		return historial;
	}
}
