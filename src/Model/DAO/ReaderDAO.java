package Model.DAO;

import Model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;
import util.ReaderStatementBuilder;
import util.SQLStatementBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {

    private static SQLStatementBuilder builder = new ReaderStatementBuilder();


    //SELECT a Reader
    public static Reader searchReader(String pesel) throws SQLException, ClassNotFoundException {

        String selectStatement = builder.clear().select().withWhere("pesel",pesel).build();

        //Execute the statement
        try {
            ResultSet resultSetReader = DBUtil.executeQuery(selectStatement);

            // Our resultSet has only one Reader because we used WHERE pesel=...
            // and pesel is the primary key in table readers
            return getReaderFromResultSet(resultSetReader);
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for a reader(pesel:"+pesel+"): " + e);
            throw e;
        }
    }

    private static Reader getReaderFromResultSet(ResultSet resultSet) throws SQLException {
        Reader reader = null;

        if(resultSet.next()){
            reader = new Reader();
            reader.setPesel(resultSet.getString("pesel"));
            reader.setName(resultSet.getString("name"));
            reader.setSurname(resultSet.getString("surname"));
            reader.setEmail(resultSet.getString("email"));
            reader.setBirthday(resultSet.getDate("birthday"));
        }
        return reader;
    }

    //SELECT All Readers
    public static ObservableList<Reader> searchAllReaders() throws SQLException, ClassNotFoundException {

        String selectStatement = builder.clear().select().build();

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getReaderObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    //SELECT Readers with given Name and Surname
    public static ObservableList<Reader> searchReaders(String name, String surname) throws SQLException, ClassNotFoundException {
        String selectStatement = null;

        selectStatement = builder.clear().select().withWhere("name", name).withWhere("surname", surname).build();

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getReaderObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    private static ObservableList<Reader> getReaderObservableList(ResultSet resultSet) throws SQLException, ClassNotFoundException{

        ObservableList<Reader> readerObservableList = FXCollections.observableArrayList();

        while(resultSet.next()){
            Reader reader = new Reader();

            reader.setPesel(resultSet.getString("pesel"));
            reader.setName(resultSet.getString("name"));
            reader.setSurname(resultSet.getString("surname"));
            reader.setEmail(resultSet.getString("email"));
            reader.setBirthday(resultSet.getDate("birthday"));

            readerObservableList.add(reader);
        }

        return readerObservableList;
    }

    //Update Reader's Email Address
    private static void updateReaderEmail(String pesel, String email) throws SQLException, ClassNotFoundException {
        /*
        TODO
        Change updateStatement to DB transaction
        */
        String updateStatement =
                "BEGIN\n" +
                "   UPDATE readers\n" +
                "      SET EMAIL = '"+email+"'\n" +
                "    WHERE pesel = "+pesel+";\n" +
                "   COMMIT;\n" +
                "END;";
        //Execute update statement
        try{
            DBUtil.executeUpdate(updateStatement);
        } catch (SQLException e){
            System.out.println("Error occurred during UPDATE query: " + e);
            throw e;
        }
    }

    //Insert a Reader
    public static boolean insertReader (String pesel, String name, String surname, String email) throws SQLException, ClassNotFoundException {
        String updateStatement = null;

        List<String> values = new ArrayList<>();
        values.add(pesel);
        values.add(name);
        values.add(surname);
        values.add(email);

        updateStatement = builder.clear().insert("readers", values).build();

        //Execute update statement
        try {
            DBUtil.executeUpdate(updateStatement);
        } catch (SQLException e) {
            System.out.print("Error occurred during INSERT query: " + e);
            return false;
        }
        return true;
    }
}
