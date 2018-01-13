package Model.DAO;

import util.DBUtil;

import java.sql.SQLException;

public class ReaderDAOAdmin extends ReaderDAO {

    //DELETE a READER
    public static void deleteReader(String pesel) throws SQLException, ClassNotFoundException {
        /*
        TODO
        Change statement syntax to match DB Transaction
         */
        String deleteStatement =
                "BEGIN\n" +
                        "   DELETE FROM readers\n" +
                        "         WHERE pesel ="+pesel+";\n" +
                        "   COMMIT;\n" +
                        "END;";

        //Execute update statement
        try {
            DBUtil.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            System.out.print("Error occurred during DELETE query: " + e);
            throw e;
        }
    }
}
