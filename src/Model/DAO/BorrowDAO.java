package Model.DAO;

import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDAO {

    public static void borrow(String bookID, String pesel) throws SQLException, ClassNotFoundException {
        String updateStatement1 = "INSERT INTO BORROWS(bookID, pesel) VALUES("+bookID+", '"+pesel+"');";
        String updateStatement2 = "UPDATE Books SET status='borrowed' WHERE bookID="+bookID+";";

        //Execute update statement
        try {
            DBUtil.executeUpdate(updateStatement1);
            DBUtil.executeUpdate(updateStatement2);
        } catch (SQLException e) {
            System.out.print("Error occurred during INSERT query: " + e);
            throw e;
        }
    }

    public static void returnBook(String bookID) throws SQLException, ClassNotFoundException {
        String updateStatement = "UPDATE Books SET status='free' WHERE bookID="+bookID+"";

        //Execute update statement
        try {
            DBUtil.executeUpdate(updateStatement);
        } catch (SQLException e) {
            System.out.print("Error occurred during UPDATE query: " + e);
            throw e;
        }
    }

    public static String getPesel(String bookID) throws SQLException, ClassNotFoundException {
        String sqlStatement = "SELECT * FROM Borrows WHERE bookID="+bookID+"";

        //Execute update statement
        try {
            ResultSet rs = DBUtil.executeQuery(sqlStatement);
            if(rs.next()){
                return rs.getString("pesel");
            }
        } catch (SQLException e) {
            System.out.print("Error occurred during UPDATE query: " + e);
            throw e;
        }
        return "";
    }
}