package org.example.bricks_breacker_final_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lobby.fxml")));
        primaryStage.setTitle("Bricks Breaker Quest");

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/bricks_breacker_final_project/photos/ball44.jpg"))));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
