package Controllers;

import Model.DAO.TitleDAO;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import Model.Title;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BrowseTitlesController implements SceneController{

    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;
    private Stage window;

    private ObservableList<Title> titleObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TableView<Title> tableView;

    @FXML
    private TableColumn<Title, String> isbnColumn;

    @FXML
    private TableColumn<Title, String> authorColumn;

    @FXML
    private TableColumn<Title, String> titleColumn;

    @FXML
    private TableColumn<Title, String> publisherColumn;

    @FXML
    private TableColumn<Title, Integer> yearColumn;

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

        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().titleISBNProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().titleAuthorProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleTitleProperty());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().titlePublisherProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().titleYearProperty().asObject());
    }

    @FXML
    public void handleSearch(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        titleObservableList.clear();
        try{

            if(isbnTextField.getText().isEmpty() && titleTextField.getText().isEmpty() && authorTextField.getText().isEmpty() && publisherTextField.getText().isEmpty() && yearTextField.getText().isEmpty()){
                this.titleObservableList.addAll(TitleDAO.searchAllTitles());
            }

            else if(!isbnTextField.getText().isEmpty()){
                this.titleObservableList.addAll(TitleDAO.searchTitle(isbnTextField.getText()));
            }

            else if(isbnTextField.getText().isEmpty()) {
                this.titleObservableList.addAll(TitleDAO.searchTitles(authorTextField.getText(), titleTextField.getText(), publisherTextField.getText(), yearTextField.getText()));
            }

            tableView.setItems(titleObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        titleObservableList.clear();
        this.window.setScene(sceneStorage.get("menu"));
    }

    public void handleGetCopies(ActionEvent actionEvent) {
        Title title = tableView.getSelectionModel().getSelectedItem();
        System.out.println(title.getAuthor());
    }
}
