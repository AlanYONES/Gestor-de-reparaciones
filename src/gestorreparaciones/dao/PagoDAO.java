package gestorreparaciones.dao;

import gestorreparaciones.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.modelo.Reparacion;
import gestorreparaciones.modelo.Pago;

import gestorreparaciones.enums.TipoPago;
import gestorreparaciones.enums.FormaDePago;

public class PagoDAO {
	public void guardar(Pago pago) throws SQLException{
		String sql = "INSERT INTO pagos (reparacion_id, monto, fecha, forma_pago, tipo_pago) "
					+ "VALUES (?, ?, ?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, pago.getReparacion().getId());
			stmt.setDouble(2, pago.getMonto());
			stmt.setTimestamp(3, Timestamp.valueOf(pago.getFecha()));
			stmt.setString(4, pago.getFormaPago().name());
			stmt.setString(5, pago.getTipoPago().name());
			
			stmt.executeUpdate();
			try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if(generatedKeys.next()) {
					pago.setId(generatedKeys.getInt(1));
				}
			}
		}
	}
	
	public Pago buscarPorId(int id) throws SQLException{
		String sql = "SELECT id, reparacion_id, monto, fecha, forma_pago, tipo_pago, recargo_porcentaje, anulado "
					+ "FROM pagos "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearPago(rs);
				}
			}
		} 
		return null;
	}
	
	public List<Pago> buscarPorReparacion(int reparacionId)throws SQLException{
		String sql = "SELECT id, reparacion_id, monto, fecha, forma_pago, tipo_pago, recargo_porcentaje, anulado "
				+ "FROM pagos "
				+ "WHERE reparacion_id = ?";
		List<Pago> pagos = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, reparacionId);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					pagos.add(mapearPago(rs));
				}
			}
		}
		return pagos;
	}
	
	public double buscarIngresoPorFecha(LocalDate desde, LocalDate hasta)throws SQLException{
		String sql = "SELECT SUM(monto) AS ingresos "
				+ "FROM pagos "
				+ "WHERE fecha >= ? "
				+ "AND fecha < ?";
		double recaudado = 0;
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setTimestamp(1, Timestamp.valueOf(desde.atStartOfDay()));
			stmt.setTimestamp(2, Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					recaudado = rs.getDouble("ingresos");
				}
			}
		}
		return recaudado;
	}
	
	public void actualizarAnulado(Pago pago) throws SQLException{
		String sql = "UPDATE pagos SET anulado = ? "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setBoolean(1, pago.isPagoAnulado());
			stmt.setInt(2, pago.getId());
			stmt.executeUpdate();
		}
	}
	
	public Pago mapearPago(ResultSet rs) throws SQLException{
		int reparacionId = rs.getInt("reparacion_id");
		ReparacionDAO daoReparacion = new ReparacionDAO();
		Reparacion reparacion = daoReparacion.buscarPorId(reparacionId);
		
		Pago pago = new Pago(
				reparacion,
				rs.getDouble("monto"),
				FormaDePago.valueOf(rs.getString("forma_pago")),
				TipoPago.valueOf(rs.getString("tipo_pago"))
				);
		pago.setId(rs.getInt("id"));
		pago.setRecargoPorcentaje(rs.getDouble("recargo_porcentaje"));
		pago.setAnulado(rs.getBoolean("anulado"));
		pago.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
		return pago;
	}
}
