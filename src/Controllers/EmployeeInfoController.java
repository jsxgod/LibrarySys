package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.AccessLevel;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeInfoController implements SceneController{

    public ListView<Object> employeeInfoList;

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    private String pesel;
    private String name;
    private String surname;
    private String email;
    private int salary;
    private int accessLevel;

    private List<Object> employeeInfoArray = new ArrayList<>();

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Employee Info");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void getParams(String pesel, String name, String surname, String email, int salary, int accessLevel){
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.salary = salary;
        this.accessLevel = accessLevel;

        /*
        TODO
        SQL Query here to get the information about the given Reader
         */

        employeeInfoArray.add("Pesel: " + this.pesel);
        employeeInfoArray.add("Name: " + this.name);
        employeeInfoArray.add("Surname: " + this.surname);
        employeeInfoArray.add("Email: " + this.email);
        employeeInfoArray.add("Salary: " + this.salary);
        employeeInfoArray.add("AccessLevel: " + AccessLevel.values()[accessLevel].toString());

        ObservableList<Object> observableArrayList = FXCollections.observableArrayList(employeeInfoArray);

        this.employeeInfoList.setItems(observableArrayList);
    }

    public void handleBack(ActionEvent actionEvent) {
        employeeInfoArray.clear();
        window.setScene(sceneStorage.get("browseEmployees"));
        window.centerOnScreen();
    }
}
