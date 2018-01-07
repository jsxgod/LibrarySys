package Controllers;

import Storage.ControllerStorage;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import TableRows.ReaderRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseReadersController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private ControllerStorage controllerStorage;

    @FXML
    private TableColumn peselColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn surnameColumn;

    @FXML
    private Button searchButton;

    @FXML
    public TableView<ReaderRow> tableView;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Browse Readers");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
        this.controllerStorage = ControllerStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleSearch(ActionEvent actionEvent) {

    }

    public void handleSelect(ActionEvent actionEvent) {
        ReaderRow selectedReader = tableView.getSelectionModel().getSelectedItem();
        if(selectedReader != null){
            ( (ReaderInfoController) controllerStorage.get("readerInfo") ).getParams(selectedReader.getPesel(),selectedReader.getName(),selectedReader.getSurname());
            window.setScene(sceneStorage.get("readerInfo"));
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("menu"));
    }
}
