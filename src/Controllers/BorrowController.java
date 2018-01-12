package Controllers;

import Model.Book;
import Model.DAO.BookDAO;
import Model.DAO.BorrowDAO;
import Model.DAO.ReaderDAO;
import Model.DAO.TitleDAO;
import Model.Reader;
import Model.Title;
import Storage.ParameterStorage;
import Storage.SceneStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.ReaderStatus;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BorrowController implements SceneController{

    private Stage window;
    private ParameterStorage parameterStorage;
    private SceneStorage sceneStorage;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField bookIDTextField;

    @Override
    public void setupController(Stage window) {
        this.window = window;
        this.window.setTitle("Borrow Menu");
        this.parameterStorage = ParameterStorage.getInstance();
        this.sceneStorage = SceneStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleBorrow(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Alert borrowAlert =  new Alert(null);
        Book book = BookDAO.searchBook(bookIDTextField.getText());
        Title title = TitleDAO.searchTitle(book.getIsbn());
        String bookID = bookIDTextField.getText();
        String status;

        try {
            if (BookDAO.isBorrowed(bookID)) {
                borrowAlert.setAlertType(Alert.AlertType.INFORMATION);
                borrowAlert.setTitle("Cannot Borrow");
                borrowAlert.setHeaderText("Book with given ID: "+bookID+" is already borrowed by "+ReaderDAO.searchReader(BorrowDAO.getPesel(bookID)).getNameAndSurname()+".");
                borrowAlert.setContentText(ReaderDAO.searchReader(BorrowDAO.getPesel(bookID)).getNameAndSurname() + " - " + BorrowDAO.getPesel(bookID));
                borrowAlert.showAndWait();
            } else {
                try{
                    status = ReaderDAO.searchReader(peselTextField.getText()).getStatus();
                } catch (NullPointerException e){
                    status = "";
                    borrowAlert.setAlertType(Alert.AlertType.WARNING);
                    borrowAlert.setHeaderText("Failure");
                    borrowAlert.setContentText("Given PESEL does not belong to a Reader.");
                    borrowAlert.showAndWait();
                }
                switch (status){
                    case "INACTIVE":
                        ReaderDAO.setActive(peselTextField.getText());
                        BorrowDAO.borrow(bookIDTextField.getText(), peselTextField.getText());
                        book.setReader(peselTextField.getText());

                        borrowAlert.setAlertType(Alert.AlertType.INFORMATION);
                        borrowAlert.setTitle("Borrowed Successfully");
                        borrowAlert.setContentText("Borrowed " + title.getTitle() + " with ID: " + book.getId() + " to " + book.getReader());
                        borrowAlert.showAndWait();
                        break;
                    case "ACTIVE":
                        BorrowDAO.borrow(bookIDTextField.getText(), peselTextField.getText());
                        book.setReader(peselTextField.getText());

                        borrowAlert.setAlertType(Alert.AlertType.INFORMATION);
                        borrowAlert.setTitle("Borrowed Successfully");
                        borrowAlert.setContentText("Borrowed " + title.getTitle() + " with ID: " + book.getId() + " to " + book.getReader());
                        borrowAlert.showAndWait();
                        break;
                    case "SUSPENDED":
                        borrowAlert.setAlertType(Alert.AlertType.WARNING);
                        borrowAlert.setHeaderText("Failure");
                        borrowAlert.setContentText("Given PESEL belongs to a SUSPENDED Reader.");
                        borrowAlert.showAndWait();
                }


                bookIDTextField.clear();
                peselTextField.clear();
                window.setScene(sceneStorage.get("currentMenu"));
                window.setTitle("Menu");
                window.centerOnScreen();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        bookIDTextField.clear();
        peselTextField.clear();

        window.setScene(sceneStorage.get("currentMenu"));
        window.setTitle("Menu");
        window.centerOnScreen();
    }
}
