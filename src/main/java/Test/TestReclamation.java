package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestReclamation extends Application {

    public static void main(String[] args) {
        launch(args);  // Lance l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationPage.fxml"));
        Parent root = loader.load();  // Charge le contenu du fichier FXML
        Scene scene = new Scene(root); // Crée une scène à partir du fichier FXML
        primaryStage.setTitle("Gestion des Reclamations");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}