package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddReaderController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    private List<TextField> textFieldList = new ArrayList<TextField>();

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private DatePicker birthdayDatePicker;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Add Reader");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleConfirm(ActionEvent actionEvent) {
        /*
        TODO
        Add user to the DB
        Alert if Reader added correctly
         */

        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        emailTextField.clear();

        window.setScene(sceneStorage.get("menu"));
        window.centerOnScreen();
    }
}
