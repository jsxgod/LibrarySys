package Controllers;

import Model.DAO.BookDAO;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TitleInfoController implements SceneController{

    public ListView<Object> titleInfoList;

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    private List<Object> titleInfoArray = new ArrayList<>();

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Title Info");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void getParams(String isbn, String author, String title, String publisher) throws SQLException, ClassNotFoundException {

        titleInfoArray.add("ISBN: " + isbn);
        titleInfoArray.add("Author: " + author);
        titleInfoArray.add("Title: " + title);
        titleInfoArray.add("Publisher: " + publisher);

        List<String> ids = BookDAO.getIds(isbn);
        String idsPrepared = String.join(", ", ids);
        titleInfoArray.add("IDs: " + idsPrepared);

        ObservableList<Object> observableArrayList = FXCollections.observableArrayList(titleInfoArray);

        this.titleInfoList.setItems(observableArrayList);
    }

    public void handleBack(ActionEvent actionEvent) {
        titleInfoArray.clear();
        window.setScene(sceneStorage.get("browseTitles"));
        window.centerOnScreen();
    }
}
