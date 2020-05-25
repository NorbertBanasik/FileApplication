package fileapp.controller;

import fileapp.crypt.Crypter;
import fileapp.crypt.Mode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static fileapp.crypt.Mode.DECRYPT;
import static fileapp.crypt.Mode.ENCRYPT;

public class StartController {

//  Moje ---------------------------------------------------
    private static final String NAPISE = "Szyfruj";
    private static final String NAPISD = "Odszyfruj";

    private static final String FILE_TYPE = ".crypt";

    private static Crypter crypter;

    public static Crypter getCrypter() {
        return crypter;
    }

    private static Mode wybor;

    public static Mode getWybor() {
        return wybor;
    }

    private static File inFile, outFile;

    public static File getInFile() {
        return inFile;
    }

    public static File getOutFile() {
        return outFile;
    }

    private Stage stage;

    private File wybranyPlik;

    private static Thread workingThread;

    public static Thread getWorkingThread() {
        return workingThread;
    }

//  FXML-------------------------------------------------------
    @FXML
    private ChoiceBox<String> cbMode;

    @FXML
    private TextField podgladPliku;

    @FXML
    private TextField podgladPliku1;

    @FXML
    private Button btnWybierz;

    @FXML
    private Button btnCloseStart;

    @FXML
    private Button btnMinStart;

    @FXML
    private Button btnStart;

    @FXML
    private TextField komunikat;

    @FXML
    private Button btnDecryptStart;

    @FXML
    public void initialize() {

        podgladPliku.textProperty().addListener(fileListener);
        cbMode.valueProperty().addListener(modeListener);
        cbMode.setValue(NAPISE);
        wybor = ENCRYPT;
        crypter = new Crypter();
    }

    private void mapInputsToFiles() {
        inFile = new File(podgladPliku.getText());
        outFile = new File(podgladPliku1.getText());
    }

    private ChangeListener<String> fileListener = new ChangeListener<>() {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue == null) {
                return;
            }

            String path = podgladPliku.getText();

            if (path.isEmpty()) {
                podgladPliku1.setText("");
                return;
            }

            switch (wybor) {
                case ENCRYPT:
                    path = path + FILE_TYPE;
                    podgladPliku1.setText(path);
                    break;

                case DECRYPT:
                    path = path.replace(FILE_TYPE, "");
                    podgladPliku1.setText(path);
                    break;
            }

            mapInputsToFiles();

        }
    };

    private ChangeListener<String> modeListener = new ChangeListener<>() {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue == null) {
                return;
            }

            switch (newValue) {
                case NAPISE:
                    wybor = ENCRYPT;
                    komunikat.setText(null);
                    fileListener.changed(null, null, podgladPliku.getText());
                    break;

                case NAPISD:
                    wybor = DECRYPT;
                    komunikat.setText(null);
                    fileListener.changed(null, null, podgladPliku.getText());
                    break;
            }

        }
    };

    public void onActionCloseStart(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onActionMinimize(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void onActionWybierz(ActionEvent actionEvent) {

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filtrRozszerzenia = new FileChooser.ExtensionFilter("Pliki tekstowe (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(filtrRozszerzenia);
        FileChooser.ExtensionFilter filtrRozszerzenia2 = new FileChooser.ExtensionFilter("Pliki zaszyfrowane (*.crypt)", "*.crypt");
        chooser.getExtensionFilters().add(filtrRozszerzenia2);
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            podgladPliku.setText(file.getAbsolutePath());
            komunikat.setText("");
            mapInputsToFiles();
        }
        
        if(file.getAbsolutePath().endsWith(FILE_TYPE) && Mode.ENCRYPT.equals(wybor)){
            komunikat.setText("Plik " + file.getName() + " jest już zaszyfrowany.");
        }

    }

    public void onActionStart(ActionEvent actionEvent) throws IOException {

            if(podgladPliku.getText().length() == 0) {
                komunikat.setText("Wybierz plik");
            }
            else{

            Parent view = FXMLLoader.load(getClass().getResource("/Sceny/MainView.fxml"));
            Scene scene = new Scene(view);
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            window.show();

            new SceneDrag(scene);}

    }

}
