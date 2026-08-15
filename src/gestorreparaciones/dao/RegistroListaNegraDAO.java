package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.modelo.RegistroListaNegra;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Empleado;

public class RegistroListaNegraDAO {
	
	public void guardar(RegistroListaNegra registro) throws SQLException{
		String sql = "INSERT INTO registro_lista_negra (cliente_id, motivo, fecha, empleado_id) "
					+ "VALUES (?, ?, ?, ?)";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, registro.getCliente().getId());
			stmt.setString(2, registro.getMotivo());
			stmt.setDate(3, Date.valueOf(registro.getFecha()));
			stmt.setInt(4, registro.getEmpleadoQueRegistro().getId());
			
			stmt.executeUpdate();
			try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if(generatedKeys.next()) {
					registro.setId(generatedKeys.getInt(1));
				}
			}
		}
	}
	
	public RegistroListaNegra buscarPorId(int id) throws SQLException{
		String sql = "SELECT id, cliente_id, motivo, fecha, empleado_id "
					+ "FROM registro_lista_negra "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearRegistro(rs);
				}
			}
		}
		return null;
	}
	
	public List<RegistroListaNegra> buscarPorCliente(int id) throws SQLException{
		String sql = "SELECT id, cliente_id, motivo, fecha, empleado_id "
					+ "FROM registro_lista_negra "
					+ "WHERE cliente_id = ?";
		List<RegistroListaNegra> registros = new ArrayList<>();
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					registros.add(mapearRegistro(rs));
				}
			}
		}
		return registros;
	}
	

	
	
	
	
	public RegistroListaNegra mapearRegistro(ResultSet rs) throws SQLException{
		int clienteId = rs.getInt("cliente_id");
		int empleadoId = rs.getInt("empleado_id");
		
		ClienteDAO daoCliente = new ClienteDAO();
		EmpleadoDAO daoEmpleado = new EmpleadoDAO();
		
		Cliente cliente = daoCliente.buscarPorId(clienteId);
		Empleado empleado = daoEmpleado.buscarPorId(empleadoId);
		
		RegistroListaNegra registro = new RegistroListaNegra(
				cliente,
				rs.getString("motivo"),
				rs.getDate("fecha").toLocalDate(),
				empleado
				);
		registro.setId(rs.getInt("id"));
		return registro;
	}
}
