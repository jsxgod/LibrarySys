package Controllers;

import Model.DAO.EmployeeDAO;
import Model.DAO.ReaderDAO;
import Model.DAO.UserDAO;
import Model.Employee;
import Storage.ControllerStorage;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import Model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BrowseEmployeesController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private ControllerStorage controllerStorage;

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    public TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> surnameColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    @FXML
    private TableColumn<Employee, Integer> salaryColumn;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Browse Readers");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
        this.controllerStorage = ControllerStorage.getInstance();

        try {
            employeeObservableList.addAll(EmployeeDAO.searchAllEmployees());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tableView.setItems(employeeObservableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        /*
        TODO
        IF the application freezes because of bigger amounts of data
        multithreading: Create executor that uses daemon threads:
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        */
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        salaryColumn.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());
    }


    public void handleSelect(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if(selectedEmployee != null){
            ( (EmployeeInfoController) controllerStorage.get("employeeInfo") )
                    .getParams(selectedEmployee.getPesel(),
                            selectedEmployee.getName(),
                            selectedEmployee.getSurname(),
                            selectedEmployee.getEmail(),
                            selectedEmployee.getSalary(),
                            UserDAO.searchUser(selectedEmployee.getPesel()).getAccessLevel());
            window.setScene(sceneStorage.get("employeeInfo"));
            window.setTitle("Employee Info");
            window.centerOnScreen();
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("currentMenu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
