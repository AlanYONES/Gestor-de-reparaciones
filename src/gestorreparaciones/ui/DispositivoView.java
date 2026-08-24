package gestorreparaciones.ui;

import java.util.List;

import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.enums.TipoEquipo;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.sistema.Sistema;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class DispositivoView {
	
	private final Sistema sistema;
	private TableView<Dispositivo> tabla;
	
	public DispositivoView(Sistema sistema) {
		this.sistema = sistema;
	}
	
	public Node construir() {
		tabla = crearTabla();
		cargarDatos();
		
		GridPane formulario = crearFormulario();
		
		VBox raiz = new VBox(15, new Label("Listado de dispositivos"), tabla,
								new Label("Nuevo dispositivo"), formulario);
		raiz.setPadding(new Insets(15));
		return raiz;
	}
	
	private TableView<Dispositivo> crearTabla(){
		TableView<Dispositivo> t = new TableView<>();
		
		TableColumn<Dispositivo, TipoEquipo> colTipoEquipo = new TableColumn<>("Tipo de equipo");
		colTipoEquipo.setCellValueFactory(cellData -> 
											new SimpleObjectProperty(
											cellData.getValue().getTipoEquipo()));
		
		TableColumn<Dispositivo, String> colMarca = new TableColumn<>("Marca");
		colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
		
		TableColumn<Dispositivo, String> colModelo = new TableColumn<>("Modelo");
		colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
		
		TableColumn<Dispositivo, String> colDueño = new TableColumn<>("Dueño");
		colDueño.setCellValueFactory(cellData -> 
								new SimpleStringProperty(
										cellData.getValue()
										.getCliente()
										.getNombre()));
		t.getColumns().addAll(colTipoEquipo, colMarca, colModelo, colDueño);
		return t;
	}
	
	private void cargarDatos() {
		try {
			List<Dispositivo> dispositivos = sistema.getDispositivos();
			tabla.setItems(FXCollections.observableArrayList(dispositivos));
		}catch(Exception e) {
			mostrarError(e.getMessage());
		}
	}
	
	private GridPane crearFormulario() {
		GridPane grilla = new GridPane();
		grilla.setHgap(10);
		grilla.setVgap(10);
		
		ComboBox<Cliente> comboCliente = new ComboBox<>();
		try {
			comboCliente.getItems().addAll(sistema.getClientes());
		}catch(Exception e) {
			mostrarError(e.getMessage());
		}
		ComboBox<TipoEquipo> comboTipo = new ComboBox<>();
		comboTipo.getItems().addAll(TipoEquipo.values());
		comboTipo.setValue(TipoEquipo.CELULAR);
		TextField campoMarca = new TextField();
		TextField campoModelo = new TextField();
		TextField campoImei = new TextField();
		TextField campoNumeroSerie = new TextField();
		
		grilla.add(new Label("Cliente:"), 0, 0);
		grilla.add(comboCliente, 1, 0);
		grilla.add(new Label("Tipo:"), 0, 1);
		grilla.add(comboTipo, 1, 1);
		grilla.add(new Label("Marca:"), 0, 2);
		grilla.add(campoMarca, 1, 2);
		grilla.add(new Label("Modelo:"), 0, 3);
		grilla.add(campoModelo, 1, 3);
		grilla.add(new Label("Imei:"), 0, 4);
		grilla.add(campoImei, 1, 4);
		grilla.add(new Label("Numero de serie:"), 0, 5);
		grilla.add(campoNumeroSerie, 1, 5);
		
		Button botonGuardar = new Button("Guardar dispositivo");
		botonGuardar.setOnAction(evento -> {
			try {
				Dispositivo nuevo = new Dispositivo(comboCliente.getValue(), comboTipo.getValue(), 
													campoMarca.getText(), campoModelo.getText(), 
													campoImei.getText(), campoNumeroSerie.getText());
				sistema.agregarDispositivo(nuevo);
				cargarDatos();
				campoMarca.clear();
				campoModelo.clear();
				campoImei.clear();
				campoNumeroSerie.clear();
			}catch(Exception e){
				mostrarError(e.getMessage());
			}
		});
		grilla.add(botonGuardar, 1, 6);
		
		return grilla;
	}
	
	private void mostrarError(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
		alerta.showAndWait();
	}

}
