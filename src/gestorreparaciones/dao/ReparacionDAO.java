package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.enums.EstadoReparacion;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.modelo.Reparacion;

public class ReparacionDAO {
	
	public void guardar(Reparacion reparacion) throws SQLException{
		String sql = "INSERT INTO reparaciones (dispositivo_id, empleado_id, estado_reparacion, "
					+ "falla_declarada, estado_fisico_al_recibir, fecha_entrada, presupuesto) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, reparacion.getDispositivo().getId());
			stmt.setInt(2, reparacion.getEmpleado().getId());
			stmt.setString(3, reparacion.getEstado().name());
			stmt.setString(4, reparacion.getFallaDeclarada());
			stmt.setString(5, reparacion.getEstadoFisicoAlRecibir());
			stmt.setTimestamp(6, Timestamp.valueOf(reparacion.getFechaEntrada()));
			stmt.setDouble(7, reparacion.getPresupuesto());
			
			stmt.executeUpdate();
			try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if(generatedKeys.next()) {
					reparacion.setId(generatedKeys.getInt(1));
				}
			}	
		}
	}

	public Reparacion buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, dispositivo_id, empleado_id, estado_reparacion, falla_declarada, "
					+ "estado_fisico_al_recibir, observaciones, reparacion_realizada, "
					+ "fecha_entrada, fecha_entrega_estimada, fecha_entrega_final, presupuesto, "
					+ "pin_desbloqueo, tiene_garantia, dias_garantia, fecha_vencimiento_garantia, "
					+ "cancelada_con_cargo, cargo_revision "
					+ "FROM reparaciones "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearReparacion(rs);
				}
			}
		}
		return null;
	}
	
	public List<Reparacion> buscarTodos() throws SQLException{
		String sql = "SELECT id, dispositivo_id, empleado_id, estado_reparacion, falla_declarada, "
				+ "estado_fisico_al_recibir, observaciones, reparacion_realizada, "
				+ "fecha_entrada, fecha_entrega_estimada, fecha_entrega_final, presupuesto, "
				+ "pin_desbloqueo, tiene_garantia, dias_garantia, fecha_vencimiento_garantia, "
				+ "cancelada_con_cargo, cargo_revision "
				+ "FROM reparaciones ";
		List<Reparacion> reparaciones = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					reparaciones.add(mapearReparacion(rs));
				}
			}
		}
		return reparaciones;
	}
	
	public List<Reparacion> buscarPorDispositivo(int dispositivoId) throws SQLException{
		String sql = "SELECT id, dispositivo_id, empleado_id, estado_reparacion, falla_declarada, "
				+ "estado_fisico_al_recibir, observaciones, reparacion_realizada, "
				+ "fecha_entrada, fecha_entrega_estimada, fecha_entrega_final, presupuesto, "
				+ "pin_desbloqueo, tiene_garantia, dias_garantia, fecha_vencimiento_garantia, "
				+ "cancelada_con_cargo, cargo_revision "
				+ "FROM reparaciones "
				+ "WHERE dispositivo_id = ?";
		List<Reparacion> reparaciones = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, dispositivoId);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					reparaciones.add(mapearReparacion(rs));
				}
			}
		}
		return reparaciones;
	}
	
	public List<Reparacion> buscarPorEstado(EstadoReparacion estado) throws SQLException{
		String sql = "SELECT id, dispositivo_id, empleado_id, estado_reparacion, falla_declarada, "
				+ "estado_fisico_al_recibir, observaciones, reparacion_realizada, "
				+ "fecha_entrada, fecha_entrega_estimada, fecha_entrega_final, presupuesto, "
				+ "pin_desbloqueo, tiene_garantia, dias_garantia, fecha_vencimiento_garantia, "
				+ "cancelada_con_cargo, cargo_revision "
				+ "FROM reparaciones "
				+ "WHERE estado_reparacion = ?";
		List<Reparacion> reparaciones = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, estado.name());
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					reparaciones.add(mapearReparacion(rs));
				}
			}
		}
		return reparaciones;
	}
	
	public void actualizarCancelacion(Reparacion reparacion) throws SQLException{
		String sql = "UPDATE reparaciones "
					+ "SET estado_reparacion = ?, "
					+ "cancelada_con_cargo = ?, "
					+ "cargo_revision = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, reparacion.getEstado().name());
			stmt.setBoolean(2, reparacion.isCanceladaConCargo());
			stmt.setDouble(3, reparacion.getCargoRevision());
			stmt.setInt(4, reparacion.getId());
			
			stmt.executeUpdate();
		}
	}
	
	public void actualizarEstado(Reparacion reparacion) throws SQLException{
		String sql = "UPDATE reparaciones "
					+ "SET estado_reparacion = ?, "
					+ "fecha_entrega_final = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, reparacion.getEstado().name());
			stmt.setTimestamp(2, Timestamp.valueOf(reparacion.getFechaEntregaFinal()));
			stmt.setInt(3, reparacion.getId());
			
			stmt.executeUpdate();
		}
	}
	
	public void actualizarGarantia(Reparacion reparacion) throws SQLException{
		String sql = "UPDATE reparaciones "
					+ "SET tiene_garantia = ?, "
					+ "dias_garantia = ?, "
					+ "fecha_vencimiento_garantia = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setBoolean(1, reparacion.isTieneGarantia());
			stmt.setInt(2, reparacion.getDiasGarantia());
			stmt.setDate(3, Date.valueOf(reparacion.getFechaVencimientoGarantia()));
			stmt.setInt(4, reparacion.getId());
			
			stmt.executeUpdate();
		}
	}
	
	public void actualizarObservacionesYFecha(Reparacion reparacion) throws SQLException{
		String sql = "UPDATE reparaciones "
					+ "SET observaciones = ?, "
					+ "fecha_entrega_estimdada = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, reparacion.getObservaciones());
			stmt.setDate(2, Date.valueOf(reparacion.getFechaEntregaEstimada()));
			stmt.setInt(3, reparacion.getId());
			
			stmt.executeUpdate();
		}
	}
	
	public Reparacion mapearReparacion(ResultSet rs) throws SQLException{
		int empleadoId = rs.getInt("empleado_id");
		int dispositivoId = rs.getInt("dispositivo_id");
		
		EmpleadoDAO empleadoDAO = new EmpleadoDAO();
		Empleado empleado = empleadoDAO.buscarPorId(empleadoId);
		
		DispositivoDAO dispositivoDAO = new DispositivoDAO();
		Dispositivo dispositivo = dispositivoDAO.buscarPorId(dispositivoId);
		
		Reparacion reparacion = new Reparacion(
			dispositivo,
			empleado,
			rs.getString("falla_declarada"),
			rs.getString("estado_fisico_al_recibir"),
			rs.getDouble("presupuesto")
		);
		reparacion.setId(rs.getInt("id"));
		reparacion.setEstado(EstadoReparacion.valueOf(rs.getString("estado_reparacion")));
		reparacion.setFechaEntrada(rs.getTimestamp("fecha_entrada").toLocalDateTime());
		return reparacion;
	}
}
