package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements SceneController {

    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private Stage window;

    private String login;
    private String password;

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private VBox vbox;

    public LoginController() throws IOException{

    }


    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Login");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        /*
        TODO
        Check if user exists in DB
        Check if the password is correct
         */

        this.login = loginInput.getText();
        this.password = passwordInput.getText();

        Alert passwordAlert;
        if (passwordInput.getText().equals("123")) {
            window.setScene(sceneStorage.get("menu"));
        }
        else {
            passwordAlert = new Alert(Alert.AlertType.ERROR);
            passwordAlert.setTitle("Password Alert");
            passwordAlert.setContentText("Incorrect Login and Password Combination");
            passwordAlert.showAndWait();
        }

        loginInput.clear();
        passwordInput.clear();
    }
}
