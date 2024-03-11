package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnString {
    private static final String JDBC_URL = "jdbc:sqlserver://CHAOCHIAHSUAN-N;databaseName=APITest;encrypt=false";
    private static final String USERNAME = "ad";
    private static final String PASSWORD = "ad";

//    public static String getJdbcUrl() {
//        return JDBC_URL;
//    }
//
//    public static String getUsername() {
//        return USERNAME;
//    }
//
//    public static String getPassword() {
//        return PASSWORD;
//    }
    
    public static Connection getConnection() throws SQLException {
        String url = JDBC_URL;
        String username = USERNAME;
        String password = PASSWORD;
        return DriverManager.getConnection(url, username, password);
    }
}