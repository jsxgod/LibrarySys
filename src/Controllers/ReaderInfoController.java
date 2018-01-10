package Controllers;

import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReaderInfoController implements SceneController{

    public ListView<Object> readerInfoList;

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    private String pesel;
    private String name;
    private String surname;
    private String email;
    private Date birthday;

    private List<Object> readerInfoArray = new ArrayList<>();

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

    public void getParams(String pesel, String name, String surname, String email, Date birthday){
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthday = birthday;

        /*
        TODO
        SQL Query here to get the information about the given Reader
         */

        readerInfoArray.add(this.pesel);
        readerInfoArray.add(this.name);
        readerInfoArray.add(this.surname);
        readerInfoArray.add(this.email);
        readerInfoArray.add(this.birthday);

        ObservableList<Object> observableArrayList = FXCollections.observableArrayList(readerInfoArray);

        this.readerInfoList.setItems(observableArrayList);
    }

    public void handleBack(ActionEvent actionEvent) {
        readerInfoArray.clear();
        window.setScene(sceneStorage.get("browseReaders"));
        window.centerOnScreen();
    }
}
