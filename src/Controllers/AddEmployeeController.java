package Controllers;

import Model.DAO.EmployeeDAO;
import Model.DAO.UserDAO;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AccessLevel;
import util.PasswordGenerator;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class AddEmployeeController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private TextField bankAccountTextField;

    @FXML
    private ComboBox<AccessLevel> accessLevelComboBox;


    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Add Employee");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
        accessLevelComboBox.getItems().addAll(AccessLevel.ADMINISTRATOR, AccessLevel.SUPERVISOR, AccessLevel.LIBRARIAN);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    @FXML
    private void handleConfirm(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Alert addEmployeeAlert = new Alert(null);

        Random randomGenerator = new Random();
        PasswordGenerator pGenerator = new PasswordGenerator();
        String salt = String.valueOf(randomGenerator.nextInt());
        String hashedPassword = pGenerator.hashFunction(passwordTextField.getText(), salt);
        System.out.println(nameTextField.getText());

        if(EmployeeDAO.insertEmployee(peselTextField.getText(), nameTextField.getText(), surnameTextField.getText(), emailTextField.getText(), salaryTextField.getText(), bankAccountTextField.getText())){

            UserDAO.insertUser(peselTextField.getText(), hashedPassword, salt, String.valueOf(accessLevelComboBox.getValue().ordinal()));

            addEmployeeAlert.setAlertType(Alert.AlertType.INFORMATION);
            addEmployeeAlert.setTitle("Employee Added");
            addEmployeeAlert.setContentText("Successfully Added a new Employee");
            addEmployeeAlert.showAndWait();

            peselTextField.clear();
            nameTextField.clear();
            surnameTextField.clear();
            passwordTextField.clear();
            salaryTextField.clear();
            bankAccountTextField.clear();

            window.setScene(sceneStorage.get("currentMenu"));
            window.setTitle("Menu");
            window.centerOnScreen();
        }
        else{
            addEmployeeAlert.setAlertType(Alert.AlertType.ERROR);
            addEmployeeAlert.setTitle("Employee Not Added");
            addEmployeeAlert.setHeaderText("Error occurred during the operation.");
            addEmployeeAlert.setContentText("Check if the data is correct and try again.");
            addEmployeeAlert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("currentMenu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
