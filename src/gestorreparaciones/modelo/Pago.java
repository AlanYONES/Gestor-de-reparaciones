package gestorreparaciones.modelo;

import java.time.LocalDate;

import gestorreparaciones.enums.FormaDePago;
import gestorreparaciones.enums.TipoPago;

public class Pago {
	private int id;
	private Reparacion reparacion;
	private double monto;
	private LocalDate fecha;
	private FormaDePago formaPago;
	private TipoPago tipoPago;
	
}
