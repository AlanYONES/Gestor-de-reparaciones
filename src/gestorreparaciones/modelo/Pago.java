package gestorreparaciones.modelo;

import java.time.LocalDateTime;

import gestorreparaciones.enums.FormaDePago;
import gestorreparaciones.enums.TipoPago;

public class Pago {
	private int id;
	private Reparacion reparacion;
	private double monto;
	private LocalDateTime fecha;
	private FormaDePago formaPago;
	private TipoPago tipoPago;
	private double recargoPorcentaje;
	private boolean anulado;
	
	public Pago(Reparacion reparacion, double monto, FormaDePago formaPago, TipoPago tipoPago) {
		this.id = -1;
		this.reparacion = reparacion;
		this.monto = monto;
		this.fecha = LocalDateTime.now();
		this.formaPago = formaPago;
		this.tipoPago = tipoPago;
		this.recargoPorcentaje = 0.0;
		this.anulado = false;
	}

	public Reparacion getReparacion() {
		return reparacion;
	}
	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public FormaDePago getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(FormaDePago formaPago) {
		this.formaPago = formaPago;
	}
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}
	public double getRecargoPorcentaje() {
		return recargoPorcentaje;
	}
	public void setRecargoPorcentaje(double recargoPorcentaje) {
		this.recargoPorcentaje = recargoPorcentaje;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double montoConRecargo() {
        return monto + (monto * (recargoPorcentaje / 100.0));
    }
	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}
	public void setAnularPago() {
		this.anulado = true;
	}
	public boolean isPagoAnulado() {
		return this.anulado;
	}
	@Override
	public String toString() {
		return String.format("Pago #%d - Monto: %%.2f - Fecha: %s - Forma de pago: %s - Tipo de pago: %s", 
								id, montoConRecargo(),fecha, formaPago, tipoPago);
	}
}
