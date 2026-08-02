package gestorreparaciones.modelo;

public class GarantiaInfo {
	private Dispositivo dispositivo;
	private long diasRestantes;
	
	public GarantiaInfo(Dispositivo dispositivo, long diasRestantes) {
		super();
		this.dispositivo = dispositivo;
		this.diasRestantes = diasRestantes;
	}
	
	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}
	public long getDiasRestantes() {
		return diasRestantes;
	}
	public void setDiasRestantes(long diasRestantes) {
		this.diasRestantes = diasRestantes;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s - Garantia vence en %d dia(s).",
								dispositivo.getMarca(), dispositivo.getModelo(), this.diasRestantes);
	}
	
}
