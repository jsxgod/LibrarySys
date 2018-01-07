package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import TableRows.TitleRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowseTitlesController implements SceneController{

    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private Stage window;


    @FXML
    private TableView<TitleRow> tableView;

    @FXML
    private TableColumn authorColumn;

    @FXML
    private TableColumn titleColumn;

    @FXML
    private TableColumn isbnColumn;

    @FXML
    private Button searchButton;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Title Browser");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleSearch(ActionEvent actionEvent) {
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        this.window.setScene(sceneStorage.get("menu"));
    }

    public void handleGetCopies(ActionEvent actionEvent) {
        TitleRow titleRow = tableView.getSelectionModel().getSelectedItem();
        System.out.println(titleRow.getAuthor());
    }
}
