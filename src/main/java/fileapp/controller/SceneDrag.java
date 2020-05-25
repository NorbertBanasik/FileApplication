package fileapp.controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class SceneDrag {

    private double xOffset ;
    private double yOffset ;

    public SceneDrag(Scene scene) {

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            scene.getWindow().setX(e.getScreenX() - xOffset);
            scene.getWindow().setY(e.getScreenY() - yOffset);
        });
    }
}
