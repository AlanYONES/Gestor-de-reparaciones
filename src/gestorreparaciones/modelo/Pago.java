package gestorreparaciones.modelo;

import java.time.LocalDate;

import gestorreparaciones.enums.FormaDePago;
import gestorreparaciones.enums.TipoPago;

public class Pago {
	private static int contadorId = 0;
	private final int id;
	private Reparacion reparacion;
	private double monto;
	private LocalDate fecha;
	private FormaDePago formaPago;
	private TipoPago tipoPago;
	private double recargoPorcentaje;
	
	public Pago(Reparacion reparacion, double monto, LocalDate fecha, FormaDePago formaPago, TipoPago tipoPago) {
		this.id = contadorId++;
		this.reparacion = reparacion;
		this.monto = monto;
		this.fecha = fecha;
		this.formaPago = formaPago;
		this.tipoPago = tipoPago;
		this.recargoPorcentaje = 0.0;
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
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
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
	public int getId() {
		return id;
	}
	public double montoConRecargo() {
        return monto + (monto * (recargoPorcentaje / 100.0));
    }
}
