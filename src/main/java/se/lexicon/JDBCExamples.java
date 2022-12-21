package se.lexicon;

import se.lexicon.exception.DBConnectionException;

import java.sql.*;
import java.time.LocalDate;

public class JDBCExamples {

    public static void main(String[] args) {
    ex5();
    }

    //Statement
    public static void ex1(){
        String query = "select * from task";

        /*Connection connection = null;
        try {

            connection = MySQLConnection.getConnection();
            // connection is autocloseable interface

        } catch (DBConnectionException e) {
            System.out.println(e.getMessage());
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }*/

        //try with resources to close automatically connection
        try (
                Connection connection = MySQLConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ){
            while (resultSet.next()){
                System.out.print(resultSet.getInt("id")+"\t");
                System.out.print(resultSet.getString("title")+"\t");
                System.out.print(resultSet.getString("_description")+"\n");
                System.out.println("------------------");
            }

        } catch (DBConnectionException | SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    //PreparedStatement
    public static void ex2(){
        System.out.println("Ex2");
        String query1 = " select * from task where id = ? and title like ?";
        // ? -> replace any one character

        //String query2 = " select * from task where id = ? and title = ?";

        //String query3 = " select * from task where id = ? and title like ?";


        int taskId = 3;
        // if we use a condition (where id = ?) -> PreparedStatement

        try(
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                //PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                //PreparedStatement preparedStatement3 = connection.prepareStatement(query3);

                ){

            preparedStatement1.setInt(1, taskId);
            //preparedStatement1.setInt(1, 5);

            preparedStatement1.setString(2, "t"+"%");
            // we assign to parameterIndex 1 "value" of taskId(3)

            //in case of query2
            //preparedStatement2.setInt(1, 3);
            //preparedStatement2.setString(2, "task3");
            //in case of query3
            //preparedStatement3.setString(2, "t%");  // x -> ("t"+"%")

            try(ResultSet resultSet1 = preparedStatement1.executeQuery();) {
                //no param here -> we set the param before (after line with Connection)
                // because ResultSet is outside () from try -> it is not closing automatically
                // -> we will include these lines into a try(with resources) block without catch
                // and the while loop as a body {while}


                while (resultSet1.next()) {
                    System.out.print(resultSet1.getInt("id") + "\t");
                    System.out.print(resultSet1.getString("title") + "\t");
                    System.out.print(resultSet1.getString("_description") + "\n");
                    System.out.println("------------------");
                }
            }

        }catch (DBConnectionException | SQLException e){
            System.out.println(e.getMessage());

        }
    }

    //update / DELETE
    public static void ex3(){
        int taskID =5;
        String query = "DELETE FROM TASK WHERE ID = ?";

        try (
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                ) {
            preparedStatement.setInt(1, taskID);
            int rowAffected = preparedStatement.executeUpdate();

            //executeQuery -> SELECT
            //executeUpdate -> modify (Delete)
            //executeUpdate return how many rows are affected
            System.out.println(rowAffected);

    }catch (DBConnectionException | SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    //Insert data to database
    public static void ex4(){
        String query = "INSERT INTO TASK(title, _description) VALUES (?,?)";

        try (
                Connection connection = MySQLConnection.getConnection();
                //PreparedStatement preparedStatement = connection.prepareStatement(query);


                //in order to get the id of the new row: Statement.RETURN_GENERATED_KEYS
                PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

                ){
            preparedStatement.setString(1, "TEST9");
            preparedStatement.setString(2, "DESC9");

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);

            // to get the generated key (id of the new row
            try(
            ResultSet resultSet = preparedStatement.getGeneratedKeys();) {
                //if we add ResultSet to try(resources), it will close automatically

                if (resultSet.next()) { // if the row exists
                    System.out.println("Task ID is:" + resultSet.getInt(1));
                }
            }
        }catch (DBConnectionException|SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        }

    public static void ex5(){

        String queryAddress = "insert into address(city, zip_code) values(? , ?)";
        String queryPerson ="insert into person(first_name, last_name, email, birth_date, address_id) values(?,?,?,?,?)";

        try(
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatementAddress = connection.prepareStatement(queryAddress, Statement.RETURN_GENERATED_KEYS);

                ){

            connection.setAutoCommit(false);
            // if we write a wrong table name(e.g) we don't want to continue
            //-> we declare an interrupt connection in the beginning -> we don't have connection with DB
            // we will set connection (true) after we run without error these 2 queries

            preparedStatementAddress.setString(1,"TEST");
            preparedStatementAddress.setString(2,"12345");

            preparedStatementAddress.executeUpdate(); // insert the new address

            Integer addressId = null; //declared outside try block in order to have assign to person
            try(ResultSet resultSet = preparedStatementAddress.getGeneratedKeys()) {

                if (resultSet.next()) {
                    addressId = resultSet.getInt(1);
                }
            }
            // here we had inserted the address -> we obtained addressID
            // -> next we have to insert the person

            if (addressId == null){
                //if we can not insert a new address - we don't get a new addressID
                // -> we can not continue to insert a person
                throw new RuntimeException("getGeneratedKeys value for address query was null!");
            }

            try(
                    PreparedStatement preparedStatementPerson = connection.prepareStatement(queryPerson, Statement.RETURN_GENERATED_KEYS);

            ){
                preparedStatementPerson.setString(1,"TESTER");
                preparedStatementPerson.setString(2,"TESTSSON");
                preparedStatementPerson.setString(3,"TEST.TEST@TEST.TEST");
                preparedStatementPerson.setDate(4, Date.valueOf("2000-01-01"));
                //convert LocalDate from Java to Date in SQL -> Date.valueOf("    ")
                preparedStatementPerson.setInt(5, addressId);
                //assign the address to the person

                preparedStatementPerson.executeUpdate(); // insert the new person

                try(ResultSet resultSet = preparedStatementPerson.getGeneratedKeys();) {

                    if (resultSet.next()) {
                        System.out.println("Person ID : " + resultSet.getInt(1));
                    }
                }
            }
            connection.commit();
            //apply changes to DB only if we can execute these 2 queries

        }catch(DBConnectionException | SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }
}
