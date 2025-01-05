package Test;

import Controllers.CreateCompteController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        Parent root = Loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Page d'accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
