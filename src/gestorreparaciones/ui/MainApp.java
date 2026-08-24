package gestorreparaciones.ui;

import gestorreparaciones.sistema.Sistema;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final Sistema sistema = new Sistema();

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        ClientesView clientesView = new ClientesView(sistema);
        Tab tabClientes = new Tab("Clientes", clientesView.construir());
        tabClientes.setClosable(false);

        tabPane.getTabs().add(tabClientes);

        Scene escena = new Scene(tabPane, 900, 600);
        stage.setTitle("Gestor de Reparaciones");
        stage.setScene(escena);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
