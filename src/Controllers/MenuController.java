package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleSearchTitles(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseTitles"));
    }

    public void handleBorrow(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("borrow"));
    }

    public void handleReturn(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("return"));
    }

    public void handleAddTitle(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addTitle"));
    }

    public void handleAddReader(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addReader"));
    }

    public void handleLogOut(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("login"));
    }

    public void handleBrowseReaders(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseReaders"));
    }
}
