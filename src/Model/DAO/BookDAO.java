package Model.DAO;

import Model.Book;
import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {


    //SELECT a Title
    public static Book searchBook(String bookID) throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM Books WHERE bookID="+bookID+";";
        //Execute the statement
        try {
            ResultSet resultSetTitle = DBUtil.executeQuery(selectStatement);

            // Our resultSet has only one Book because we used WHERE ISBN=...
            // and ISBN is the primary key in the titles table
            return getBookFromResultSet(resultSetTitle);
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for a book(bookID:"+bookID+"): " + e);
            throw e;
        }
    }

    private static Book getBookFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Book book = null;

        if(resultSet.next()){
            book = new Book();
            book.setId(resultSet.getInt("bookID"));
            book.setIsbn(resultSet.getString("ISBN"));
        }
        return book;
    }

    //Insert a Book
    public static void insertBook (String ISBN, String numberOfCopies) throws SQLException, ClassNotFoundException {

        int numOfCopies = Integer.parseInt(numberOfCopies);
        String updateStatement = "INSERT INTO BOOKS(isbn) VALUES('"+ISBN+"');";


        while (numOfCopies > 0) {
        //Execute update statement
            try {
                DBUtil.executeUpdate(updateStatement);
            } catch (SQLException e) {
                System.out.print("Error occurred during INSERT query: " + e);
                throw e;
            }
            numOfCopies--;
        }
    }

    public static boolean isBorrowed(String bookID) throws SQLException, ClassNotFoundException {
        String selectStatement = "SELECT * FROM Books WHERE bookID="+bookID+";";
        //Execute the statement
        try {
            ResultSet resultSetBorrows = DBUtil.executeQuery(selectStatement);
        if(resultSetBorrows.next()){
            if(resultSetBorrows.getString("status").equals("free")){
                return false;
            }
            else {
                return true;
            }
        }
        else{
            return false;
        }
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for a book(bookID:"+bookID+"): " + e);
            throw e;
        }
    }

    public static List<String> getIds(String isbn) throws SQLException, ClassNotFoundException {
        String sqlStatement = "SELECT * FROM Books WHERE ISBN='"+isbn+"';";
        List<String> bookIdList = new ArrayList<>();
        try {
            ResultSet resultSetBorrows = DBUtil.executeQuery(sqlStatement);
            while(resultSetBorrows.next()){
                bookIdList.add(String.valueOf(resultSetBorrows.getInt("bookID")));
                System.out.println("got ID");
            }

            } catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                System.out.println("Could not get the list of BookIDs of selected Title");
                throw e;
            }

            return bookIdList;
    }
}

