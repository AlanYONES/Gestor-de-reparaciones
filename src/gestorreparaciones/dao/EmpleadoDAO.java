package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.Empleado;

import gestorreparaciones.enums.RolEmpleado;

public class EmpleadoDAO {
	
	public void guardar(Empleado empleado) throws SQLException {
		String sql = "INSERT INTO empleados (nombre, cuit, rol_empleado, activo) "
                + "VALUES (?, ?, ?, ?)";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			
			stmt.setString(1, empleado.getNombre());
			stmt.setString(2, empleado.getCuit());
			stmt.setString(3, empleado.getRol().name());
			stmt.setBoolean(4, empleado.isActivo());
			
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					empleado.setId(generatedKeys.getInt(1));
	            }
	        }
		}
	}
	
	public Empleado buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, nombre, cuit, rol_empleado, activo "
					+ "FROM empleados "
					+ "WHERE id = ?";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearEmpleado(rs);
				}
			}
		}
		return null;
	}
	
	public Empleado buscarPorCuit(String cuit) throws SQLException {
		String sql = "SELECT id, nombre, cuit, rol_empleado, activo "
					+ "FROM empleados "
					+ "WHERE cuit = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, cuit);
			
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearEmpleado(rs);
				}
			}
		}
		return null;
	}
	
	public List<Empleado> buscarTodos() throws SQLException{
		String sql = "SELECT id, nombre, cuit, rol_empleado, activo "
					+"FROM empleados ";
		List<Empleado> empleados = new ArrayList<>();
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					empleados.add(mapearEmpleado(rs));
				}
			}
		}
		return empleados;
	}
	
	public void cambiarActivo (int id, boolean activo) throws SQLException{
		String sql = "UPDATE empleados SET activo = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setBoolean(1, activo);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}
	
	public boolean existePorCuit(String cuit) throws SQLException{
		String sql ="SELECT id "
				+ "FROM empleados "
				+ "WHERE cuit = ?";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1, cuit);
			
			try(ResultSet rs = stmt.executeQuery()){
				return rs.next();
			}
		}
	}
	

	public Empleado mapearEmpleado(ResultSet rs) throws SQLException{
		Empleado empleado = new Empleado(
				rs.getString("nombre"),
				rs.getString("cuit"),
				RolEmpleado.valueOf(rs.getString("rol_empleado"))
				);
		empleado.setId(rs.getInt("id"));
	    empleado.setActivo(rs.getBoolean("activo"));
		return empleado;
	}
}
