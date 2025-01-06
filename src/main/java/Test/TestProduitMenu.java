package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import entités.Produit;
import entités.GestionCommande;


public class TestProduitMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML avec le contrôleur associé automatiquement par le fx:controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaceAdmin.fxml"));

        // Charger le fichier FXML et obtenir le root
        Parent root = loader.load();

        // Créer la scène et l'afficher
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestion des Produits");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Disable resizing
        primaryStage.show();
    }
}
