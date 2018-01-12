package Controllers;

import Model.Book;
import Model.Borrow;
import Model.DAO.BookDAO;
import Model.DAO.BorrowDAO;
import Model.DAO.ReaderDAO;
import Model.DAO.TitleDAO;
import Model.Title;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.ReaderStatus;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReturnController implements SceneController {

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField bookIDTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Return Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleReturn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Alert returnAlert =  new Alert(null);
        Book book = BookDAO.searchBook(bookIDTextField.getText());
        Title title = TitleDAO.searchTitle(book.getIsbn());
        String bookID = bookIDTextField.getText();

        try {
            if (!BookDAO.isBorrowed(bookID)) {
                returnAlert.setAlertType(Alert.AlertType.ERROR);
                returnAlert.setTitle("Cannot Return");
                returnAlert.setHeaderText("Failure");
                returnAlert.setContentText("Book with given ID: "+bookID+" is not borrowed.");
                returnAlert.showAndWait();
            } else {
                String pesel = BorrowDAO.getPesel(bookID);
                String status = ReaderDAO.searchReader(pesel).getStatus();

                returnAlert.setAlertType(Alert.AlertType.INFORMATION);
                returnAlert.setTitle("Returned Successfully");
                returnAlert.setContentText("Borrowed " + title.getTitle() + " with ID: " + book.getId() + " to " + book.getReader());

                switch (status){
                    case "INACTIVE":
                        book.setReader(pesel);
                        break;
                    case "ACTIVE":
                        BorrowDAO.returnBook(bookID);
                        book.setReader(null);
                        returnAlert.showAndWait();
                        break;
                    case "SUSPENDED":
                        ReaderDAO.setActive(pesel);
                        BorrowDAO.returnBook(bookID);
                        book.setReader(null);
                        returnAlert.showAndWait();
                }

                bookIDTextField.clear();
                window.setScene(sceneStorage.get("currentMenu"));
                window.setTitle("Menu");
                window.centerOnScreen();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        bookIDTextField.clear();
        window.setScene(sceneStorage.get("currentMenu"));
        window.centerOnScreen();
    }

    public void handleCancel(ActionEvent actionEvent) {
        bookIDTextField.clear();

        window.setScene(sceneStorage.get("currentMenu"));
        window.centerOnScreen();
    }
}
