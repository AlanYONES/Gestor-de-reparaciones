package gestorreparaciones.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gestorreparaciones.conexion.ConexionDB;
import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.RegistroListaNegra;

public class ClienteDAO {
	
	public void guardar(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO clientes (nombre, apellido, tipo_documento, numero_documento, telefono, correo, en_lista_negra) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
        	PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        	
        	stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTipoDocumento().name());
            stmt.setString(4, cliente.getNumeroDocumento());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getCorreo());
            stmt.setBoolean(7, cliente.isEnListaNegra());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                }
            }
        }
	}
	
	public Cliente buscarPorId(int id) throws SQLException{
		String sql ="SELECT id, nombre, apellido, tipo_documento, numero_documento, "
					+ "telefono, correo, en_lista_negra "
					+ "FROM clientes "
					+ "WHERE id = ?";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				if (rs.next()) {
					return mapearCliente(rs);
				}
			}
		}
		return null;
	}
	
	public Cliente buscarPorDocumento(String documento) throws SQLException{
		String sql ="SELECT id, nombre, apellido, tipo_documento, numero_documento, "
				+ "telefono, correo, en_lista_negra "
				+ "FROM clientes "
				+ "WHERE numero_documento = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1,documento);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return mapearCliente(rs);
				}
			}
		}
		return null;
	}
	
	public List<Cliente> filtroPorListaNegra() throws SQLException{
	    String sql = "SELECT id, nombre, apellido, tipo_documento, numero_documento, "
	               + "telefono, correo, en_lista_negra "
	               + "FROM clientes "
	               + "WHERE en_lista_negra = TRUE";
	    
		List<Cliente> clientes = new ArrayList<>();
		
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			try(ResultSet rs = stmt.executeQuery()){
				
				while(rs.next()) {
					
					clientes.add(mapearCliente(rs));
					
				}
			}
		}
		return clientes;
	}
	
	public Cliente mapearCliente(ResultSet rs) throws SQLException{
	    Cliente cliente = new Cliente(
	            rs.getString("nombre"),
	            rs.getString("apellido"),
	            TipoDocumento.valueOf(rs.getString("tipo_documento")),
	            rs.getString("numero_documento"),
	            rs.getString("telefono"),
	            rs.getString("correo")
	        );
	    cliente.setId(rs.getInt("id"));
	    cliente.setEnListaNegra(rs.getBoolean("en_lista_negra"));
	    return cliente;
	}
	
	public List<Cliente> buscarTodos() throws SQLException{
		String sql ="SELECT id, nombre, apellido, tipo_documento, numero_documento, "
				+ "telefono, correo, en_lista_negra "
				+ "FROM clientes";
		List<Cliente> clientes = new ArrayList<>();
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					clientes.add(mapearCliente(rs));
				}
			}
		}
		return clientes;
	}
	
	public void actualizar (Cliente cliente) throws SQLException {
		String sql = "UPDATE clientes SET nombre = ?, "
				+ "apellido = ?, "
				+ "tipo_documento = ?, "
				+ "numero_documento = ?, "
				+ "telefono = ?, "
				+ "correo = ?, "
				+ "en_lista_negra = ? "
	            + "WHERE id = ?";
		
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTipoDocumento().name());
            stmt.setString(4, cliente.getNumeroDocumento());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getCorreo());
            stmt.setBoolean(7, cliente.isEnListaNegra());
            stmt.setInt(8, cliente.getId());
            stmt.executeUpdate();
		}
	}
	
	public boolean existePorDocumento(String documento) throws SQLException{
		String sql ="SELECT id"
				+ "FROM clientes "
				+ "WHERE numero_documento = ?";
		try (Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1, documento);
			
			try(ResultSet rs = stmt.executeQuery()){
				return rs.next();
			}
		}
	}
	
	public void quitarListaNegra(Cliente cliente)throws SQLException{
		String sql = "UPDATE clientes SET en_lista_negra = FALSE "
				+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, cliente.getId());
			stmt.executeUpdate();
		}
	}
	public void marcarListaNegra(Cliente cliente) throws SQLException{
		String sql = "UPDATE clientes SET en_lista_negra = TRUE "
					+ "WHERE id = ?";
		try(Connection conn = ConexionDB.obtenerConexion();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, cliente.getId());
			stmt.executeUpdate();
		}
	}
	
}
