package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BorrowController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField bookIDTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Borrow Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleBorrow(ActionEvent actionEvent) {
        /*
        TODO
        check if pesel is correct and exists in DB
        check if bookID is correct (not already borrowed etc.)
        TODO
        Actually perform the borrow action if possible
         */
        bookIDTextField.clear();
        peselTextField.clear();
        window.setScene(sceneStorage.get("menu"));
        window.centerOnScreen();

    }
}
