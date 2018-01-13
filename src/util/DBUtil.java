package util;

import com.sun.rowset.CachedRowSetImpl;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.*;

public class DBUtil {

    //Java DataBase Connection Driver
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    private static Connection connection = null;

    /*
        Connection String
    *
    *   pattern: "jdbc:oracle:thin:Username/Password@IP:Port/SID";
    */

    private static final String connectionString = "jdbc:mysql://localhost/Library?" + "user=root&password=cat123";

    //Establish Connection
    public static void connect() throws SQLException, ClassNotFoundException {
        //Set JDBC DRIVER
        try{
            try {
                Class.forName(JDBC_DRIVER).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e){
            System.out.println("MySql JDBC Driver Not Found");
            e.printStackTrace();
            throw e;
        }

        System.out.println("MySQL JDBC Driver Has Been Set Successfully");

        //Establish connection
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e){
            System.out.println("Connection failed");
            e.printStackTrace();
            throw e;
        }
    }

    //Close Connection
    public static void disconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            } else {
                System.out.println("Connection closed already or is NULL");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    //Execute DB Query
    public static ResultSet executeQuery(String sqlStatement) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl cachedRowSet = null;

        try {
            connect();

            System.out.println("Select statement: " + sqlStatement + "\n");

            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sqlStatement);

            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Could not execute query: " + e);
            throw e;
        } finally {
            if(resultSet != null){
                resultSet.close();
            }

            if(stmt != null){
                stmt.close();
            }

            disconnect();
        }

        return cachedRowSet;
    }

    //Execute Update Query: Update/Insert/Delete
    public static void executeUpdate(String sqlStatement) throws SQLException, ClassNotFoundException {
        Statement stmt = null;

        try {
            connect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println("Could not execute update: " + e);
            throw e;
        } finally {
            if(stmt != null){
                stmt.close();
            }

            disconnect();
        }
    }

    public static void callProcedure(String sqlStatement) throws SQLException, ClassNotFoundException {
        CallableStatement stmt = null;

        try {
            connect();
            stmt = connection.prepareCall(sqlStatement);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Could not execute call: " + e);
            throw e;
        } finally {
            if(stmt != null){
                stmt.close();
            }

            disconnect();
        }
    }

    public static boolean backupDB(String dbName, String user, String ip, String port, String password){
        String dumpCommand = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe --routines " + dbName + " -h" + ip + " -u " + user + " -p" + password;
        Runtime runtime = Runtime.getRuntime();
        File backup = new File(System.getProperty("user.home") + "/Desktop/dump.sql");
        PrintStream printStream;
        try{
            Process child = runtime.exec(dumpCommand);
            printStream = new PrintStream(backup);
            InputStream in = child.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                printStream.write(ch);
                System.out.write(ch); //Check in console.
            }

            InputStream err = child.getErrorStream();
            while ((ch = err.read()) != -1) {
                System.out.write(ch);
            }
        }catch(Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }



    public static boolean restoreDB(String dbName, String userName, String password, String path) throws IOException, InterruptedException {
        String[] executeCmd = new String[]{"C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysql.exe", "--user=" + userName, "--password=" + password, dbName,"-e", " source "+path};
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                return true;
            } else { System.out.println("Could not restore the backup"); }
        } catch (Exception ex) { ex.printStackTrace(); }

        return false;
    }
}
