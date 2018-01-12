package Model.DAO;

import Model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    //SELECT a User
    public static Employee searchEmployee(String pesel) throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM Employees WHERE pesel='"+pesel+"';";
        //Execute the statement
        try {
            ResultSet resultSetEmployee = DBUtil.executeQuery(selectStatement);

            // Our resultSet has only one User because we used WHERE pesel=...
            // and pesel is the primary key in the users table
            return getEmployeeFromResultSet(resultSetEmployee);
        } catch (SQLException e) {
            System.out.println("Error occurred while lookig for an employee(pesel:"+pesel+"): " + e);
            throw e;
        }
    }

    //SELECT All Users
    public static ObservableList<Employee> searchAllEmployees() throws SQLException, ClassNotFoundException {

        String selectStatement = "SELECT * FROM Employees;";

        //Execute the statement
        try{
            ResultSet resultSet = DBUtil.executeQuery(selectStatement);

            return getEmployeeObservableList(resultSet);
        } catch (SQLException e){
            System.out.println("SQL SELECT Query Could Not Be Executed:" + e);
            throw e;
        }
    }

    private static ObservableList<Employee> getEmployeeObservableList(ResultSet resultSet) throws SQLException, ClassNotFoundException{

        ObservableList<Employee> userObservableList = FXCollections.observableArrayList();

        while(resultSet.next()){
            Employee employee = new Employee();

            employee.setName(resultSet.getString("name"));
            employee.setSurname(resultSet.getString("surname"));
            employee.setSalary(resultSet.getInt("salary"));
            employee.setBankAccount(resultSet.getString("bankAccount"));

            userObservableList.add(employee);
        }

        return userObservableList;
    }


    private static Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Employee employee = null;

        if(resultSet.next()){
            employee = new Employee();

            employee.setName(resultSet.getString("name"));
            employee.setSurname(resultSet.getString("surname"));
            employee.setSalary(resultSet.getInt("salary"));
            employee.setBankAccount(resultSet.getString("bankAccount"));
        }
        return employee;
    }

    public static boolean insertEmployee(String pesel, String name, String surname, String email, String salary, String bankAccount) throws SQLException, ClassNotFoundException {

        String updateStatement = "INSERT INTO Employees(pesel,name,surname,email,salary,bankAccount) VALUES ('"+pesel+"', '"+name+"', '"+surname+"', '"+email+"', "+salary+", '"+bankAccount+"');";
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
