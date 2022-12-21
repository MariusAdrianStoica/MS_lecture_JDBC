package se.lexicon;

import se.lexicon.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String JDBC_USER ="root";
    private static final String JDBC_PASS ="1234";
    private static final String JDBC_URL ="jdbc:mysql://localhost:3306/lecture_db";
    //we defined final fields in order not to change them
    // we defined static fields in order to use them directly - they don't depend on the class


    // method is responsible to communicate with database and return the connection link
    public static Connection getConnection() throws DBConnectionException{
        // here we obtain the connection to sql database

        //we get exception and we will catch it
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            //return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS); -> single line statement
            return connection;
        } catch (SQLException e) {
            throw new DBConnectionException("DB Connection failed");
        }
    }
}
