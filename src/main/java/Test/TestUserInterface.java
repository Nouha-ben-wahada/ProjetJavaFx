package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestUserInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML de l'interface utilisateur avec le contrôleur associé
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/userinterface.fxml"));

        // Charger le fichier FXML et obtenir le root
        Parent root = loader.load();

        // Créer la scène et l'afficher
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestion des Produits - Interface Utilisateur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

