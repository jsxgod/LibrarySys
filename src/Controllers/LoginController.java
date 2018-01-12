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

    private enum AccessLevel { ADMINISTRATOR, SUPERVISOR, LIBRARIAN}

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private VBox vbox;

    public LoginController() throws IOException {

    }

    private String hashFunction(String password, String salt) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
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

        try {
            User user = UserDAO.searchUser(login);
            if (user != null) {
                String salt = user.getSalt();
                String hash = user.getHash();
                if (hashFunction(password, salt).equals(hash)) {
                    loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                    loginAlert.setTitle("Login Successfully");
                    loginAlert.setContentText("login as " + user.getPesel() + " with access level " + AccessLevel.values()[user.getAccessLevel()]);
                    loginAlert.showAndWait();
                    window.setScene(sceneStorage.get("menu"));
                    window.centerOnScreen();
                } else {
                    loginAlert.setAlertType(Alert.AlertType.ERROR);
                    loginAlert.setTitle("Login error");
                    loginAlert.setContentText("Incorrect password");
                    loginAlert.showAndWait();
                }
                loginInput.clear();
                passwordInput.clear();
            }
            else{
                loginAlert.setAlertType(Alert.AlertType.ERROR);
                loginAlert.setTitle("Login error");
                loginAlert.setContentText("User do not exists");
                loginAlert.showAndWait();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
