package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuSupervisorController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleSearchTitles(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseTitles"));
        window.setTitle("Title Browser");
        window.centerOnScreen();
    }

    @FXML
    private void handleBorrow(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("borrow"));
        window.setTitle("Borrow");
        window.centerOnScreen();
    }

    @FXML
    private void handleReturn(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("return"));
        window.setTitle("Return");
        window.centerOnScreen();
    }

    @FXML
    private void handleAddTitle(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addTitle"));
        window.setTitle("Add Title");
        window.centerOnScreen();
    }

    @FXML
    private void handleAddReader(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addReader"));
        window.setTitle("Add Reader");
        window.centerOnScreen();
    }

    @FXML
    private void handleLogOut(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("login"));
        window.setTitle("Login");
        window.centerOnScreen();
    }

    @FXML
    private void handleBrowseReaders(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseReaders"));
        window.setTitle("Reader Browser");
        window.centerOnScreen();
    }

    @FXML
    private void handleBrowseEmployees(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseEmployees"));
        window.setTitle("Employee Browser");
        window.centerOnScreen();
    }

    @FXML
    private void handleAddEmployee(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addEmployee"));
        window.setTitle("Add Employee");
        window.centerOnScreen();
    }
}