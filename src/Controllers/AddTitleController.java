package Controllers;

import Model.DAO.TitleDAO;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DBUtil;

import java.net.URL;
import java.sql.SQLException;
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

    @FXML
    private TextField numberOfCopiesTextField;

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

    public void handleConfirm(ActionEvent actionEvent) throws SQLException, ClassNotFoundException{
        /*
        TODO
        Add the Title to the DB with given number of books of that Title
         */

        TitleDAO.insertTitle(isbnTextField.getText(), authorTextField.getText(), titleTextField.getText(), publisherTextField.getText(), yearTextField.getText(), numberOfCopiesTextField.getText());

        isbnTextField.clear();
        titleTextField.clear();
        authorTextField.clear();
        publisherTextField.clear();
        yearTextField.clear();

        window.setScene(sceneStorage.get("menu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("menu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
