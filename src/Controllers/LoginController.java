package Controllers;

import Model.DAO.UserDAO;
import Model.User;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.AccessLevel;
import util.PasswordGenerator;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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

    public LoginController() throws IOException {

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
        Alert loginAlert = new Alert(null);
        this.login = loginInput.getText();
        this.password = passwordInput.getText();

        PasswordGenerator pGenerator = new PasswordGenerator();

        try {
            User user = UserDAO.searchUser(login);
            if (user != null) {
                String salt = user.getSalt();
                String hash = user.getHash();
                if (pGenerator.hashFunction(password, salt).equals(hash)) {
                    loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                    loginAlert.setTitle("Login Successfully");
                    loginAlert.setContentText("login as " + user.getPesel() + " with access level " + AccessLevel.values()[user.getAccessLevel()]);
                    loginAlert.showAndWait();
                    window.setScene(sceneStorage.get("menu"));
                    window.setTitle("Menu");
                    window.centerOnScreen();
                } else {
                    loginAlert.setAlertType(Alert.AlertType.ERROR);
                    loginAlert.setTitle("Login error");
                    loginAlert.setContentText("Incorrect Login and Password combination.");
                    loginAlert.showAndWait();
                }
                loginInput.clear();
                passwordInput.clear();
            }
            else{
                loginAlert.setAlertType(Alert.AlertType.ERROR);
                loginAlert.setTitle("Login error");
                loginAlert.setContentText("User does not exists");
                loginAlert.showAndWait();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
