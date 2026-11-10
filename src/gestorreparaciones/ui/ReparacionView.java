package gestorreparaciones.ui;

import java.util.List;

import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.modelo.Dispositivo;
import gestorreparaciones.modelo.Empleado;
import gestorreparaciones.sistema.Sistema;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ReparacionView {
	private final Sistema sistema;
	
	private ComboBox<Cliente> comboCliente;
    private TextField campoNombreCliente;
    private TextField campoApellidoCliente;
    private ComboBox<TipoDocumento> comboTipoDocumento;
    private TextField campoDocumentoCliente;
    private TextField campoTelefonoCliente;
    private TextField campoCorreoCliente;
    
	private ComboBox<Dispositivo> comboDispositivo;
	private ComboBox<Empleado> comboEmpleado;
	
    

	
	
	public ReparacionView(Sistema sistema) {
		this.sistema = sistema;
	}
	
	public Node construir() {
		HBox layoutPrincipal = new HBox(20, crearColumnaIzquierda(), crearColumnaDerecha());
		layoutPrincipal.setPadding(new Insets(15));
		return layoutPrincipal;
	}
	
	private VBox crearColumnaIzquierda() {
		return new VBox(15, crearSeccionCliente());
	}
	
	private VBox crearColumnaDerecha() {
		return new VBox(15);
	}
	
	private VBox crearSeccionCliente() {
	    Label titulo = new Label("Datos Cliente");

	    comboCliente = new ComboBox<>();
	    comboCliente.setEditable(true);
	    comboCliente.setPromptText("Buscar por DNI, nombre o apellido...");

	    comboCliente.setConverter(new StringConverter<Cliente>() {
	        @Override
	        public String toString(Cliente cliente) {
	            if (cliente == null) return "";
	            return cliente.getNombre() + " " + cliente.getApellido() + " - " + cliente.getNumeroDocumento();
	        }

	        @Override
	        public Cliente fromString(String texto) {
	            return comboCliente.getSelectionModel().getSelectedItem();
	        }
	    });

	    ObservableList<Cliente> todosLosClientes = FXCollections.observableArrayList();
	    try {
	        todosLosClientes.setAll(sistema.getClientes());
	    } catch (Exception e) {
	        mostrarError(e.getMessage());
	    }

	    FilteredList<Cliente> clientesFiltrados = new FilteredList<>(todosLosClientes, c -> true);
	    comboCliente.setItems(FXCollections.observableArrayList(clientesFiltrados));

	    comboCliente.getEditor().textProperty().addListener((obs, textoAnterior, textoNuevo) -> {
	        Cliente seleccionado = comboCliente.getSelectionModel().getSelectedItem();
	        if (seleccionado != null && comboCliente.getConverter().toString(seleccionado).equals(textoNuevo)) {
	            return;   // el texto coincide con la selección actual: no es tipeo del usuario, no filtrar
	        }

	        clientesFiltrados.setPredicate(cliente -> {
	            if (textoNuevo == null || textoNuevo.isBlank()) {
	                return true;
	            }
	            return cliente.getNumeroDocumento().contains(textoNuevo)
	                || cliente.getNombre().toLowerCase().contains(textoNuevo.toLowerCase())
	                || cliente.getApellido().toLowerCase().contains(textoNuevo.toLowerCase());
	        });
	        Platform.runLater(() -> comboCliente.setItems(FXCollections.observableArrayList(clientesFiltrados)));
	    });

	    comboCliente.setCellFactory(lv -> new ListCell<>() {
	        @Override
	        protected void updateItem(Cliente cliente, boolean vacio) {
	            super.updateItem(cliente, vacio);
	            setText(vacio || cliente == null ? null :
	                    cliente.getNombre() + " " + cliente.getApellido() + " - " + cliente.getNumeroDocumento());
	        }
	    });
	    comboCliente.setButtonCell(comboCliente.getCellFactory().call(null));
	    
	    campoNombreCliente = new TextField();
	    campoApellidoCliente = new TextField();
	    comboTipoDocumento = new ComboBox<>();
	    comboTipoDocumento.getItems().addAll(TipoDocumento.values());
	    campoDocumentoCliente = new TextField();
	    campoTelefonoCliente = new TextField();
	    campoCorreoCliente = new TextField();
	    
	    comboCliente.getSelectionModel().selectedItemProperty().addListener((obs, anterior, clienteElegido) -> {
	    	 System.out.println("Listener de selección disparado. Cliente elegido: " + clienteElegido);
	        if (clienteElegido != null) {
	            // Existe: completar y bloquear
	            campoNombreCliente.setText(clienteElegido.getNombre());
	            campoApellidoCliente.setText(clienteElegido.getApellido());
	            comboTipoDocumento.setValue(clienteElegido.getTipoDocumento());
	            campoDocumentoCliente.setText(clienteElegido.getNumeroDocumento());
	            campoTelefonoCliente.setText(clienteElegido.getTelefono());
	            campoCorreoCliente.setText(clienteElegido.getCorreo());

	            setEditableCliente(false);
	        } else {
	            // No hay selección: limpiar y habilitar carga manual
	            campoNombreCliente.clear();
	            campoApellidoCliente.clear();
	            comboTipoDocumento.setValue(TipoDocumento.DNI);
	            campoDocumentoCliente.clear();
	            campoTelefonoCliente.clear();
	            campoCorreoCliente.clear();

	            setEditableCliente(true);
	        }
	    });

	    GridPane grilla = new GridPane();
	    grilla.setHgap(10);
	    grilla.setVgap(8);
	    grilla.add(new Label("Buscar:"), 0, 0);
	    grilla.add(comboCliente, 1, 0);
	    grilla.add(new Label("Nombre:"), 0, 1);
	    grilla.add(campoNombreCliente, 1, 1);
	    grilla.add(new Label("Apellido:"), 0, 2);
	    grilla.add(campoApellidoCliente, 1, 2);
	    grilla.add(new Label("Tipo doc.:"), 0, 3);
	    grilla.add(comboTipoDocumento, 1, 3);
	    grilla.add(new Label("Documento:"), 0, 4);
	    grilla.add(campoDocumentoCliente, 1, 4);
	    grilla.add(new Label("Teléfono:"), 0, 5);
	    grilla.add(campoTelefonoCliente, 1, 5);
	    grilla.add(new Label("Correo:"), 0, 6);
	    grilla.add(campoCorreoCliente, 1, 6);

	    return new VBox(5, titulo, grilla);
	}
	
	private void setEditableCliente(boolean editable) {
	    campoNombreCliente.setEditable(editable);
	    campoApellidoCliente.setEditable(editable);
	    comboTipoDocumento.setDisable(!editable);
	    campoDocumentoCliente.setEditable(editable);
	    campoTelefonoCliente.setEditable(editable);
	    campoCorreoCliente.setEditable(editable);
	}
	
	private void mostrarError(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
		alerta.showAndWait();
	}
}
