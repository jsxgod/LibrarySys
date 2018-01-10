package Model.DAO;

import Model.Title;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;
import util.SQLStatementBuilder;
import util.TitleStatementBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TitleDAO {

    private static SQLStatementBuilder builder = new TitleStatementBuilder();

    //SELECT a Title
    public static Title searchTitle(String ISBN) throws SQLException, ClassNotFoundException {

        String selectStatement = builder.clear().select().withWhere("isbn",ISBN).build();

        //Execute the statement
        try {
            ResultSet resultSetTitle = DBUtil.executeQuery(selectStatement);

            // Our resultSet has only one Title because we used WHERE ISBN=...
            // and ISBN is the primary key in the titles table
            return getTitleFromResultSet(resultSetTitle);
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for a title(ISBN:"+ISBN+"): " + e);
            throw e;
        }
    }

    private static Title getTitleFromResultSet(ResultSet resultSet) throws SQLException {
        Title title = null;

        if(resultSet.next()){
            title = new Title();
            title.setISBN(resultSet.getString("isbn"));
            title.setAuthor(resultSet.getString("authorName"));
            title.setTitle(resultSet.getString("title"));
            title.setPublisher(resultSet.getString("publisher"));
            title.setYear(resultSet.getInt("year"));
        }
        return title;
    }

    //SELECT All Readers
    public static ObservableList<Title> searchAllTitles() throws SQLException, ClassNotFoundException {

        String selectStatement = builder.clear().select().build();

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getTitleObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    //SELECT Readers with given Name and Surname
    public static ObservableList<Title> searchTitles(String author, String title, String publisher, String year) throws SQLException, ClassNotFoundException {
        String selectStatement = null;

        selectStatement = builder.clear().select().withWhere("authorName",author).withWhere("title",title).withWhere("publisher",publisher).withWhere("year",year).build();

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getTitleObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    private static ObservableList<Title> getTitleObservableList(ResultSet resultSet) throws SQLException, ClassNotFoundException{

        ObservableList<Title> titleObservableList = FXCollections.observableArrayList();

        while(resultSet.next()){
            Title title = new Title();

            title.setISBN(resultSet.getString("isbn"));
            title.setAuthor(resultSet.getString("authorName"));
            title.setTitle(resultSet.getString("title"));
            title.setPublisher(resultSet.getString("publisher"));
            title.setYear(resultSet.getInt("year"));

            titleObservableList.add(title);
        }

        return titleObservableList;
    }

    //Insert a Title
    public static void insertTitle (String ISBN, String title, String author, String publisher, String year) throws SQLException, ClassNotFoundException {

        List<String> values = new ArrayList<>();
        values.add(ISBN);
        values.add(author);
        values.add(title);
        values.add(publisher);
        values.add(year);

        String updateStatement = builder.insert("titles", values).build();

        //Execute update statement
        try {
            DBUtil.executeUpdate(updateStatement);
        } catch (SQLException e) {
            System.out.print("Error occurred during INSERT query: " + e);
            throw e;
        }
    }
}
