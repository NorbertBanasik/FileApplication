package fileapp.controller;

import fileapp.crypt.Mode;
import fileapp.crypt.StreamObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

//    Moje-------------------------------------------------
    private Thread workingThread;

    private static String haslo;

    private Mode wybor;
//  FXML-----------------------------------------------------
    @FXML
    private Button btnClose;

    @FXML
    private Button btnMin;

    @FXML
    private Button button;

    @FXML
    private Button nowy;

    @FXML
    private PasswordField pass1;

    @FXML
    private PasswordField pass2;

    @FXML
    private TextField komunikat2;

    @FXML
    private Label label;

    @FXML
    public void initialize(){
        nowy.setVisible(false);

        switch (StartController.getWybor()){
            case DECRYPT:
                button.setText("Odszyfruj");
                label.setText("Wpisz hasło aby odszyfrować");
                break;
        }
    }

    public void onActionClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void showStatus(boolean success) {
        Platform.runLater(() -> {
            if (success) {
//                komunikat2.setText("Plik został odszyfrowany.");
//                komunikat2.setStyle("-fx-text-inner-color: green; -fx-background-color: transparent");
//                pass1.setText(null);
//                pass2.setText(null);
//                button.setVisible(false);
//                nowy.setVisible(true);
//                pass1.setDisable(true);
//                pass2.setDisable(true);
            }
            else {
                komunikat2.setText("Podano złe hasło dostępu.");
                pass1.setText(null);
                pass2.setText(null);
            }
        });
    }

    public void onActionMinimize(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private Task<Boolean> encryptTask() {
        return new Task<>() {

            @Override
            protected Boolean call() throws Exception {
                boolean success;

                StreamObject streams = StartController.getCrypter().encrypt(StartController.getInFile(), StartController.getOutFile(), StartController.getCrypter().keyGen(pass1.getText()));

                try {
                    long rounds = streams.getSize() / 1024;
                    if (rounds < 1) {
                        rounds = 1L;
                    }

                    long round = 0;

                    int i;
                    byte[] b = new byte[1024];

                    while ((i = streams.getIn().read(b)) != -1) {
                        streams.getOut().write(b, 0, i);
                        updateProgress(round, rounds);
                        round++;
                    }

                    success = true;
                } catch (Exception e) {
                    success = false;
                } finally {
                    streams.close();
                }

                showStatus(success);
                return success;
            }
        };
    }

    private Task<Boolean> decryptTask() {
        return new Task<>() {

            @Override
            protected Boolean call() throws Exception {
                boolean success;

                StreamObject streams = StartController.getCrypter().decrypt(StartController.getInFile(), StartController.getOutFile(), StartController.getCrypter().keyGen(pass1.getText()));
                try {

                    long rounds = (streams.getSize() / 1024) * 2;
                    if (rounds < 1) {
                        rounds = 1L;
                    }

                    long round = 0;

                    int i;
                    byte[] b = new byte[1024];

                    while ((i = streams.getIn().read(b)) != -1) {
                        streams.getOut().write(b, 0, i);
                        updateProgress(round, rounds);
                        round++;
                    }

                    success = true;
                } catch (Exception e) {
                    success = false;
                } finally {
                    streams.close();
                }

                showStatus(success);
                return success;
            }
        };
    }


    public void onActionNowy(ActionEvent actionEvent) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/Sceny/Start.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        window.show();
    }

    public void getPass(ActionEvent actionEvent) throws InterruptedException, IOException {

        if(!((pass1.getText()).equals(pass2.getText()))) {
            komunikat2.setText("Podane hasła nie są takie same");
            pass1.setText(null);
            pass2.setText(null);
        }

        if((pass1.getText().length() == 0) || (pass2.getText().length() == 0)) {
            komunikat2.setText("Podaj hasło");
            pass1.setText(null);
            pass2.setText(null);
        }

        if(((pass1.getText()).equals(pass2.getText()))) {

            haslo = pass1.getText();

            switch (StartController.getWybor()) {

                case ENCRYPT:
                    Task<Boolean> encTask = encryptTask();
                    workingThread = new Thread(encTask, "Working-Thread");
                    workingThread.start();
                    Parent view = FXMLLoader.load(getClass().getResource("/Sceny/Success.fxml"));
                    Scene scene = new Scene(view);
                    Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    scene.setFill(Color.TRANSPARENT);
                    window.show();
                    new SceneDrag(scene);
                    break;

                    case DECRYPT:
                        if(!(pass1.getText().equals(haslo)) && !(pass2.getText().equals(haslo))){
                            System.out.println("złe hasło");
                        }
                        else{
                        Task<Boolean> decTask = decryptTask();
                        workingThread = new Thread(decTask, "Working-Thread");
                        workingThread.start();
                            Parent view1 = FXMLLoader.load(getClass().getResource("/Sceny/SuccessD.fxml"));
                            Scene scene1 = new Scene(view1);
                            Stage window1 = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                            window1.setScene(scene1);
                            scene1.setFill(Color.TRANSPARENT);
                            window1.show();
                            new SceneDrag(scene1);
                        break;
            }


        }

    }
}}
