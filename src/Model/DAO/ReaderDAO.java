package Model.DAO;

import Model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderDAO {

    private static final String EMPTY_STRING = "";

    //SELECT a Reader
    public static Reader searchReader(String pesel) throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM readers WHERE pesel=" + pesel;

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

        String selectStatement = "SELECT * FROM readers";

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

        if(!name.equals(EMPTY_STRING)){
            selectStatement = "SELECT * FROM readers WHERE name='"+name+"'";
        }
        if(!surname.equals(EMPTY_STRING)){
            selectStatement += " AND surname='"+surname+"'";
        }

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
    public static void insertReader (String pesel, String name, String surname, String email, String birthday) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        /*
        TODO
        Change updateStatement to match DB transaction
         */
        String updateStatement =
                "BEGIN\n" +
                        "INSERT INTO readers\n" +
                        "VALUES('"+pesel+"', '"+name+"', '"+surname+"','"+email+"', '"+ "STR_TO_DATE('"+birthday+"', '%d-%m-%Y'));\n" +
                        "END;";

        //Execute update statement
        try {
            DBUtil.executeUpdate(updateStatement);
        } catch (SQLException e) {
            System.out.print("Error occurred during INSERT query: " + e);
            throw e;
        }
    }
}
