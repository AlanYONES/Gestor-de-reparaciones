package gestorreparaciones.excepciones;

public class ClienteYaExistenteException extends Exception{
	private final String motivo;
	
	public ClienteYaExistenteException(String mensaje, String motivo) {
		super(mensaje);
		this.motivo = motivo;
	}
	
	public String getMotivo() {
		return this.motivo;
	}
}
