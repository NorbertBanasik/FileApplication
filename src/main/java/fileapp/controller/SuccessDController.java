package fileapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class SuccessDController {

    public void onActionClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onActionMinimize(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void onActionNew(ActionEvent actionEvent) throws IOException {

        Parent view = FXMLLoader.load(getClass().getResource("/Sceny/Start.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        window.show();

        new SceneDrag(scene);
    }
}
