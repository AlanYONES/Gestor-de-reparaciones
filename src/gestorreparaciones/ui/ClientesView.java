package gestorreparaciones.ui;

import java.util.List;

import gestorreparaciones.enums.TipoDocumento;
import gestorreparaciones.modelo.Cliente;
import gestorreparaciones.sistema.Sistema;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ClientesView {

    private final Sistema sistema;
    private TableView<Cliente> tabla;

    public ClientesView(Sistema sistema) {
        this.sistema = sistema;
    }

    public Node construir() {
        tabla = crearTabla();
        cargarDatos();

        GridPane formulario = crearFormulario();

        VBox raiz = new VBox(15, new Label("Listado de clientes"), tabla,
                              new Label("Nuevo cliente"), formulario);
        raiz.setPadding(new Insets(15));
        return raiz;
    }

    private TableView<Cliente> crearTabla() {
        TableView<Cliente> t = new TableView<>();

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, String> colDocumento = new TableColumn<>("Documento");
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        t.getColumns().addAll(colNombre, colApellido, colDocumento, colTelefono);
        return t;
    }

    private void cargarDatos() {
        try {
            List<Cliente> clientes = sistema.getClientes();
            tabla.setItems(FXCollections.observableArrayList(clientes));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private GridPane crearFormulario() {
        GridPane grilla = new GridPane();
        grilla.setHgap(10);
        grilla.setVgap(10);

        TextField campoNombre = new TextField();
        TextField campoApellido = new TextField();
        ComboBox<TipoDocumento> comboTipoDoc = new ComboBox<>();
        comboTipoDoc.getItems().addAll(TipoDocumento.values());
        comboTipoDoc.setValue(TipoDocumento.DNI);
        TextField campoDocumento = new TextField();
        TextField campoTelefono = new TextField();
        TextField campoCorreo = new TextField();

        grilla.add(new Label("Nombre:"), 0, 0);
        grilla.add(campoNombre, 1, 0);
        grilla.add(new Label("Apellido:"), 0, 1);
        grilla.add(campoApellido, 1, 1);
        grilla.add(new Label("Tipo doc.:"), 0, 2);
        grilla.add(comboTipoDoc, 1, 2);
        grilla.add(new Label("Documento:"), 0, 3);
        grilla.add(campoDocumento, 1, 3);
        grilla.add(new Label("Teléfono:"), 0, 4);
        grilla.add(campoTelefono, 1, 4);
        grilla.add(new Label("Correo:"), 0, 5);
        grilla.add(campoCorreo, 1, 5);

        Button botonGuardar = new Button("Guardar cliente");
        botonGuardar.setOnAction(evento -> {
            try {
                Cliente nuevo = new Cliente(campoNombre.getText(), campoApellido.getText(),
                        comboTipoDoc.getValue(), campoDocumento.getText(),
                        campoTelefono.getText(), campoCorreo.getText());
                sistema.agregarCliente(nuevo);
                cargarDatos();

                campoNombre.clear();
                campoApellido.clear();
                campoDocumento.clear();
                campoTelefono.clear();
                campoCorreo.clear();
            } catch (Exception e) {
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