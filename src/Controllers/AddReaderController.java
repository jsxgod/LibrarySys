package Controllers;

import Model.DAO.ReaderDAO;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
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

    public void handleConfirm(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ReaderDAO.insertReader(peselTextField.getText(), nameTextField.getText(), surnameTextField.getText(), emailTextField.getText(), birthdayDatePicker.getValue().toString());

        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        emailTextField.clear();

        window.setScene(sceneStorage.get("menu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("menu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
