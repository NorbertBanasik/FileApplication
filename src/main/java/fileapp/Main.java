package fileapp;

import fileapp.controller.SceneDrag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        Parent view = FXMLLoader.load(getClass().getResource("/Sceny/Start.fxml"));
        Scene scene = new Scene(view);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Grafiki/logo.png")));
        stage.setTitle("Ochrona plików");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        new SceneDrag(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
