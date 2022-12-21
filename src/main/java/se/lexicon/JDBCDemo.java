package se.lexicon;

import se.lexicon.model.Person;
import se.lexicon.model.Task;

import java.sql.*;
import java.time.LocalDate;

public class JDBCDemo {

    public static void main(String[] args) {

        try {
            // -> define database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lecture_db", "root", "1234");
            // connection to database lecture_db
            // password is the one we selected to open MySQl Workbench
            // Connection is an autocloseable interface

            Statement statement= connection.createStatement();
            //Statement is an autocloseable interface

            //String selectAllTasks= "select * from task";

            //String selectAllTasks= "select id, title, _description as description, person_id from task";
            // we can write directly the names of the fields from SQL table, instead of *
            // we can use even alias name -> _description (field name) "as" description(how we want to display)

            String selectAllTasks= "select t.id, t.title, t._description as description, t.person_id, p.* from task t left join person p on t.person_id=p.id";

            // -> execute query method
            // executeQuery() is used to execute SELECT query
            // executeUpdate() is used to execute UPDATE query
            ResultSet resultSet = statement.executeQuery(selectAllTasks);
            //ResultSet is an autocloseable interface

            while (resultSet.next()){


                //System.out.println(resultSet.getString("_description"));
                String title = resultSet.getString("title");
                //System.out.println(title);
                int id = resultSet.getInt("id");

                // title = exact column name from table task
                // if the type of field is varchar              -> getString
                // if the type of field is Int                  -> getInt
                // if the type of field is boolean (tinyint)    -> getBoolean
                // if the type of field is data                 -> getLocalDate

                String desc= resultSet.getString("description");
                //if we add alias name, in resultSet.getString , we have to use alias name

                int personId = resultSet.getInt("person_id");

                Task task = new Task(id, title, desc); //instantiate a task without Person


                //int personId = resultSet.getInt("id");
                // if we don't get one field from table task (person_id), we can use the same field from table person
                // -> (person_id) become (id) but we will have a NullPointerException in the left join
                String firstName = resultSet.getString("p.first_name");
                String lastName = resultSet.getString("last_name");
                // we can use the name of the field  (first_name) or tableName.fieldName (p.first_name)

                Date regDate = resultSet.getDate("p.reg_date");
                boolean active = resultSet.getBoolean("p._active");


                //instantiate a Person
                //regDate is Date, but it requires LocalDate -> regDate.toLocalDate()


                //System.out.println("TaskId: "+id+"\tTitle: "+title+"\tDescription: "+desc+"\tPersonId:"+ personId +"\tName: "+ firstName +" "+lastName + "\tRegDate: " + regDate + "\tActive: "+active);


                if (personId!=0){

                    Person person = new Person(personId, firstName, lastName, regDate.toLocalDate(), active);
                    task.setPerson(person); // assign created person to the task
                    //task 2 has no assigned person -> nullPointerException
                }

                System.out.println(task);
                System.out.println("----------------");
            }

        } catch (SQLException e) {
           System.out.println(e.getMessage());
           e.printStackTrace();
        }
    }
}
