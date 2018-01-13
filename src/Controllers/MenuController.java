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
        window.setTitle("Title Browser");
        window.centerOnScreen();
    }

    public void handleBorrow(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("borrow"));
        window.setTitle("Borrow");
        window.centerOnScreen();
    }

    public void handleReturn(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("return"));
        window.setTitle("Return");
        window.centerOnScreen();
    }

    public void handleAddTitle(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addTitle"));
        window.setTitle("Add Title");
        window.centerOnScreen();
    }

    public void handleAddReader(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("addReader"));
        window.setTitle("Add Reader");
        window.centerOnScreen();
    }

    public void handleLogOut(ActionEvent actionEvent) {
        parameterStorage.remove("currentUser");
        parameterStorage.remove("currentEmployee");
        sceneStorage.remove("currentMenu");

        window.setScene(sceneStorage.get("login"));
        window.setTitle("Login");
        window.centerOnScreen();
    }

    public void handleBrowseReaders(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("browseReaders"));
        window.setTitle("Reader Browser");
        window.centerOnScreen();
    }
}
