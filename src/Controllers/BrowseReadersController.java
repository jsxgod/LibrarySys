package Controllers;

import Model.DAO.ReaderDAO;
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

public class BrowseReadersController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private ControllerStorage controllerStorage;

    private ObservableList<Reader> readerObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    public TableView tableView;

    @FXML
    private TableColumn<Reader, String> peselColumn;

    @FXML
    private TableColumn<Reader, String> nameColumn;

    @FXML
    private TableColumn<Reader, String> surnameColumn;

    @FXML
    private TableColumn<Reader, String> emailColumn;

    @FXML
    private TableColumn<Reader, Date> birthdayColumn;

    @FXML
    private Button searchButton;

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

        peselColumn.setCellValueFactory(cellData -> cellData.getValue().readerPeselProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().readerNameProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().readerSurnameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().readerEmailProperty());
        birthdayColumn.setCellValueFactory(cellData -> cellData.getValue().readerBirthdayProperty());
    }

    public void handleSearch(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        readerObservableList.clear();
        try{

            if(peselTextField.getText().isEmpty() && nameTextField.getText().isEmpty() && surnameTextField.getText().isEmpty()){
                readerObservableList.addAll(ReaderDAO.searchAllReaders());
            }

            else if(!peselTextField.getText().isEmpty()){
                readerObservableList.addAll(ReaderDAO.searchReader(peselTextField.getText()));
            }

            else if(peselTextField.getText().isEmpty()) {
                readerObservableList.addAll(ReaderDAO.searchReaders(nameTextField.getText(), surnameTextField.getText()));
            }

            tableView.setItems(readerObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void handleSelect(ActionEvent actionEvent) {
        Reader selectedReader = (Reader) tableView.getSelectionModel().getSelectedItem();
        if(selectedReader != null){
            ( (ReaderInfoController) controllerStorage.get("readerInfo") )
                    .getParams(selectedReader.getPesel(),
                            selectedReader.getName(),
                            selectedReader.getSurname(),
                            selectedReader.getEmail(),
                            selectedReader.getBirthday(),
                            selectedReader.getStatus());
            window.setScene(sceneStorage.get("readerInfo"));
            window.centerOnScreen();
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        readerObservableList.clear();
        window.setScene(sceneStorage.get("menu"));
        window.centerOnScreen();
    }
}
