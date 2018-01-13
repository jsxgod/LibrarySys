package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.DBUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BackupController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleBackup(ActionEvent actionEvent) {
        DBUtil.backupDB("Library","root","localhost","3306","cat123");
    }

    @FXML
    private void handleRestore(ActionEvent actionEvent) throws IOException, InterruptedException {
        DBUtil.restoreDB("Library", "root", "cat123", "C://Users/smyczakgej/Desktop/dump.sql");
    }

    public void handleBack(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("currentMenu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
