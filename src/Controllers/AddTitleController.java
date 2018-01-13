package Controllers;

import Model.DAO.BookDAO;
import Model.DAO.TitleDAO;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DBUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddTitleController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

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
    private TextField numberOfCopiesTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Add Title");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleConfirm(ActionEvent actionEvent) throws SQLException, ClassNotFoundException{

        Alert addTitleAlert = new Alert(null);
        int numberOfCopies = Integer.parseInt(numberOfCopiesTextField.getText());

        if(numberOfCopiesTextField.getText().isEmpty() || numberOfCopiesTextField.getText().equals("0")){
            addTitleAlert.setAlertType(Alert.AlertType.WARNING);
            addTitleAlert.setTitle("Number of Books");
            addTitleAlert.setContentText("Remember to input the number of books of given Title.");
            addTitleAlert.showAndWait();
        } else {
            if (TitleDAO.insertTitle(isbnTextField.getText(), authorTextField.getText(), titleTextField.getText(), publisherTextField.getText(), yearTextField.getText(), numberOfCopiesTextField.getText())) {
                addTitleAlert.setAlertType(Alert.AlertType.INFORMATION);
                addTitleAlert.setTitle("Title Added");
                addTitleAlert.setContentText("Successfully Added a new Title");
                addTitleAlert.showAndWait();

                //Add the given number of copies to the Library.Books table
                BookDAO.insertBook(isbnTextField.getText(), numberOfCopiesTextField.getText());

                isbnTextField.clear();
                titleTextField.clear();
                authorTextField.clear();
                publisherTextField.clear();
                yearTextField.clear();

                window.setScene(sceneStorage.get("currentMenu"));
                window.setTitle("Menu");
                window.centerOnScreen();
            } else {
                addTitleAlert.setAlertType(Alert.AlertType.ERROR);
                addTitleAlert.setTitle("Title Not Added");
                addTitleAlert.setHeaderText("Error occurred during the operation.");
                addTitleAlert.setContentText("Check if the data is correct and try again.");
                addTitleAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        window.setScene(sceneStorage.get("currentMenu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
