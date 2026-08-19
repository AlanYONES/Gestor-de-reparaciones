package gestorreparaciones.pruebas;

import java.util.List;

import gestorreparaciones.dao.PlantillaDiagnosticoDAO;
import gestorreparaciones.modelo.PlantillaDiagnostico;

public class PruebaPlantillaDiagnosticoDAO {

    public static void main(String[] args) {
        try {
            PlantillaDiagnosticoDAO dao = new PlantillaDiagnosticoDAO();

            PlantillaDiagnostico p1 = new PlantillaDiagnostico(
                    "Cambio de pantalla OLED",
                    "Reemplazo de módulo de pantalla OLED completo, incluye testeo táctil", 3);
            PlantillaDiagnostico p2 = new PlantillaDiagnostico(
                    "Recepción completa / diagnóstico inicial",
                    "Cliente solicita diagnóstico técnico general antes de autorizar reparación", 2);

            dao.guardar(p1);
            dao.guardar(p2);
            System.out.println("Guardadas, ids: " + p1.getId() + ", " + p2.getId());

            System.out.println("\n--- Buscar todos ---");
            List<PlantillaDiagnostico> todas = dao.buscarTodos();
            todas.forEach(p -> System.out.println(p.getNombre() + " - " + p.getDiasEstimados() + " días"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}