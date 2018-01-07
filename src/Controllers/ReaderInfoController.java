package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReaderInfoController implements SceneController{

    public ListView<String> readerInfoList;

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    private String pesel;
    private String name;
    private String surname;

    private List<String> readerInfoArray = new ArrayList<String>();

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("User Info");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void getParams(String pesel, String name, String surname){
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;

        /*
        TODO
        SQL Query here to get the information about the given Reader
         */

        readerInfoArray.add(this.pesel);
        readerInfoArray.add(this.name);
        readerInfoArray.add(this.surname);

        ObservableList<String> observableArrayList = FXCollections.observableArrayList(readerInfoArray);

        this.readerInfoList.setItems(observableArrayList);
    }

    public void handleBack(ActionEvent actionEvent) {
        readerInfoArray.clear();
        window.setScene(sceneStorage.get("browseReaders"));
    }
}
