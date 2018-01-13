package Model.DAO;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {


    //SELECT an User
    public static User searchUser(String pesel) throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM Users WHERE pesel='"+pesel+"';";
        //Execute the statement
        try {
            ResultSet resultSetUser = DBUtil.executeQuery(selectStatement);

            // Our resultSet has only one User because we used WHERE pesel=...
            // and pesel is the primary key in the users table
            return getUserFromResultSet(resultSetUser);
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for a user(pesel:"+pesel+"): " + e);
            throw e;
        }
    }

    //SELECT All Users
    public static ObservableList<User> searchAllUsers() throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM users;";

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getUserObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    private static ObservableList<User> getUserObservableList(ResultSet resultSet) throws SQLException, ClassNotFoundException{

        ObservableList<User> userObservableList = FXCollections.observableArrayList();

        while(resultSet.next()){
            User user = new User();

            user.setPesel(resultSet.getString("pesel"));
            //user.setUsername(resultSet.getString("username"));
            user.setHash(resultSet.getString("hash"));
            user.setSalt(resultSet.getString("salt"));
            user.setAccessLevel(resultSet.getInt("perm"));

            userObservableList.add(user);
        }

        return userObservableList;
    }


    private static User getUserFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        User user = null;

        if(resultSet.next()){
            user = new User();
            user.setPesel(resultSet.getString("pesel"));
            //user.setUsername(resultSet.getString("username"));
            user.setHash(resultSet.getString("hash"));
            user.setSalt(resultSet.getString("salt"));
            user.setAccessLevel(resultSet.getInt("perm"));
        }
        return user;
    }

    public static boolean insertUser(String pesel, String hash, String salt, String perm) throws SQLException, ClassNotFoundException {

        int accessLevel = Integer.parseInt(perm);

        String updateStatement = "INSERT INTO Users(pesel,hash,salt,perm) VALUES ('"+pesel+"', '"+hash+"', '"+salt+"', "+accessLevel+");";

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
