package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField bookIDTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Return Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleReturn(ActionEvent actionEvent) {
        /*
        TODO
        Check if the bookID is borrowed.
        Check if user can be unbanned.
        Produce and Alert if user can be unbanned(Do not perform the unban here, just inform the LibSys user)
        TODO
        Actually perform the return action in the DB.
         */
        bookIDTextField.clear();
        window.setScene(sceneStorage.get("menu"));
        window.centerOnScreen();
    }

    public void handleCancel(ActionEvent actionEvent) {
        bookIDTextField.clear();

        window.setScene(sceneStorage.get("menu"));
        window.centerOnScreen();
    }
}
