package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseEmployeesController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Browse Employees");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
