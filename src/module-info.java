/**
 * 
 */
/**
 * 
 */
module GestorReparaciones {
	requires java.sql;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.fxml;
	
	exports gestorreparaciones.ui;
	opens gestorreparaciones.modelo to javafx.base;
}