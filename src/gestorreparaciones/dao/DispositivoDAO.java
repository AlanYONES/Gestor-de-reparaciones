package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;

import gestorreparaciones.enums.TipoEquipo;

public class DispositivoDAO {
	
	public void guardar (Dispositivo dispositivo) throws SQLException {
		String sql = "INSERT INTO dispositivos (cliente_id, tipo_equipo, marca, modelo, imei, numero_serie) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, dispositivo.getCliente().getId());
			stmt.setString(2, dispositivo.getTipoEquipo().name());
			stmt.setString(3, dispositivo.getMarca());
			stmt.setString(4, dispositivo.getModelo());
			stmt.setString(5, dispositivo.getImei());
			stmt.setString(6, dispositivo.getNumeroSerie());
			
			stmt.executeUpdate();
			try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if(generatedKeys.next()) {
					dispositivo.setId(generatedKeys.getInt(1));
				}
			}
		}
	}
	
	public Dispositivo buscarPorId (int id) throws SQLException{
		String sql = "SELECT id, cliente_id, tipo_equipo, marca, modelo, imei, numero_serie  "
					+ "FROM dispositivos "
					+ "WHERE id = ?";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearDispositivo(rs);
				}
			}
		}
		return null;
	}
	
	public List<Dispositivo> buscarTodos() throws SQLException {
		String sql = "SELECT id, cliente_id, tipo_equipo, marca, modelo, imei, numero_serie "
				    + "FROM dispositivos";
		List<Dispositivo> dispositivos = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					dispositivos.add(mapearDispositivo(rs));
				}
			}
		}
		return dispositivos;
	}
	
	public List<Dispositivo> buscarPorModelo(String modelo) throws SQLException {
		String sql = "SELECT id, cliente_id, tipo_equipo, marca, modelo, imei, numero_serie "
					+ "FROM dispositivos "
					+ "WHERE modelo = ?";
		List<Dispositivo> dispositivos = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, modelo);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					dispositivos.add(mapearDispositivo(rs));
				}
			}
		}
		return dispositivos;
	}
	
	public Dispositivo buscarPorImei(String imei) throws SQLException{
		String sql = "SELECT id, cliente_id, tipo_equipo, marca, modelo, imei, numero_serie "
					+ "FROM dispositivos "
					+ "WHERE imei = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, imei);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearDispositivo(rs);
				}
			}
		}
		return null;
	}
	
	public List<Dispositivo> buscarPorCliente(int idCliente) throws SQLException{
		String sql = "SELECT id, cliente_id, tipo_equipo, marca, modelo, imei, numero_serie "
					+ "FROM dispositivos "
					+ "WHERE cliente_id = ?";
		List<Dispositivo> dispositivos = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, idCliente);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					dispositivos.add(mapearDispositivo(rs));
				}
			}
		}
		return dispositivos;
	}
	
	public boolean existePorImei(String imei) throws SQLException{
		String sql = "SELECT id "
					+ "FROM dispositivos "
					+ "WHERE imei = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, sql);
			try(ResultSet rs = stmt.executeQuery()){
				return rs.next();
			}
		}
	}
	
	public Dispositivo mapearDispositivo (ResultSet rs) throws SQLException{
		int clienteId = rs.getInt("cliente_id");
		ClienteDAO dao = new ClienteDAO();
		Cliente cliente = dao.buscarPorId(clienteId);
		
		Dispositivo dispositivo = new Dispositivo(
				cliente,
				TipoEquipo.valueOf(rs.getString("tipo_equipo")),
				rs.getString("marca"),
				rs.getString("modelo"),
				rs.getString("imei"),
				rs.getString("numero_serie")
		);
		dispositivo.setId(rs.getInt("id"));
		return dispositivo;
				
	}
}
