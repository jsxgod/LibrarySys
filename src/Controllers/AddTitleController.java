package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddTitleController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField yearTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Add Title");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleConfirm(ActionEvent actionEvent) {
        /*
        TODO
        Add the Title to the DB with given number of books of that Title
         */

        isbnTextField.clear();
        titleTextField.clear();
        authorTextField.clear();
        publisherTextField.clear();
        yearTextField.clear();

        window.setScene(sceneStorage.get("menu"));
    }
}
