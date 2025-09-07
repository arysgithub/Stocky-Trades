package com.company.financeflowapp.mySQL;

import java.sql.*;

public class ConnectToDB {
  //define connection string property
    static final String connectionUrl = "jdbc:mysql://localhost:3306/mydb?user=root&password=Kheyaan@12";
    //String connectionUrl = "jdbc:mysql://localhost:3306; databaseName=mydb;
    // integratedSecurity=true;";


    //declare the jbdc connection objects as properties of this class
    //these establish connection and querry the DB, then auto imports
    Connection connectionObj = null;
    Statement statementObj = null;
    ResultSet resultSetObj = null;

    /**
      Establishes a connection to the database using JDBC. It attempts to load the MySQL JDBC driver
      and establish a connection to the specified URL. If successful, it prints a confirmation message.
      If it fails, it catches and prints the exceptions.
     */
    public void establishConnection() { //needs to be a method so can work
        try {
            //Class.forName… line is needed to define the driver needed to connect to SQL server.
            //This will then allow you to establish a connection using DriverManager.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectionObj = DriverManager.getConnection(connectionUrl);
            System.out.println("Successfully Connected to DB"+ connectionObj);
        } catch (Exception e) {
            System.out.println("Connection failed: Error 101");
            e.printStackTrace(); //goes directly to what went wrong
        }
    }

    /**
      Executes a SQL query against the database and returns the ResultSet. This method creates a
      statement from the established connection and executes the provided SQL query string.
      SQLQuery -The SQL query string to be executed.
      return A ResultSet object containing the data produced by the given query; never null.
     */
    public ResultSet runQuery(String SQLQuery) {
        // function takes a query as string  parameter and executes it
        try {
            statementObj = connectionObj.createStatement();
            //needs to create a statement, then execute the query
            resultSetObj = statementObj.executeQuery(SQLQuery); //connected works now
            System.out.println("run querry working");
        } catch (Exception e) {
            System.out.println("Query failed: Error 102");
            e.printStackTrace();
        }
        //when you run a query it returns a result set back
        return resultSetObj;
    }

    /**
     Executes an SQL statement such as INSERT, UPDATE, DELETE, or DDL statements. It creates a
      statement and executes the SQL command, returning the number of rows affected by the query.
     executeSQLQuery - The SQL query to execute.
     created a new int function for inserting to table becoz it returns an int
     return The number of database rows affected by the query.
     */
    public int executeUpdateCode(String executeSQLQuery) {
        int resultSetInt= 0;
        try {
            statementObj = connectionObj.createStatement();
            //however to insert info into the DB table you use executeUpdate
            resultSetInt = statementObj.executeUpdate(executeSQLQuery);
            System.out.println("Inserted the details correctly" );

        } catch (Exception e) {
            System.out.println("Sorry.Something went wrong");
            System.out.println("Query failed: Error 102");
            e.printStackTrace();
        }
        //when you run a query it returns a result set back
        return resultSetInt;
    }

    /**
      Executes a DELETE SQL statement using a PreparedStatement to safely delete records based on
      parameters. It binds the given stockID to the query and executes, returning the number of rows
      affected.
      deleteSQlQuery- The DELETE SQL query.
       stockID- The ID of the stock to delete.
      return The number of rows affected by the DELETE operation.
     */
    public int executeDeleteQuery(String deleteSQlQuery, String stockID) {
        int resultRowIntAffected = 0; // Initialize the variable to store the number of rows affected
        try {
            // Prepare the DELETE query using a PreparedStatement
            PreparedStatement statement = connectionObj.prepareStatement(deleteSQlQuery);
            // Set the stockID parameter
            statement.setString(1, stockID);
            // Execute the DELETE query and get the number of rows affected
            resultRowIntAffected = statement.executeUpdate();
            // Print a success message if the query is executed without errors
            System.out.println("Deleted the details correctly");

            // Close the PreparedStatement
            statement.close();
        } catch (SQLException e) { // Handle SQL exceptions
            // Print an error message if something goes wrong during query execution
            System.out.println("Sorry. Something went wrong");
            System.out.println("Delete query failed: Error 102");
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }

        return resultRowIntAffected; // Return the number of rows affected by the DELETE operation
    }

    /**
      Closes the database connection and releases all related resources. It safely closes the
      ResultSet, Statement, and Connection objects, catching and ignoring any errors during the
      close operations.
     */
    public void closeConnection(){
        //Good practise is to release the resources used
        //so you don’t have problems connecting to database next time you run a program.
        //Necessary to clean up all your connections and release the resources.
        if (resultSetObj!=null){try{resultSetObj.close();}catch (Exception e){} }
        if (statementObj!=null){try{statementObj.close();}catch (Exception e){} }
        if (connectionObj!=null){try{connectionObj.close();}catch (Exception e){} }
        System.out.println("Connection closed");
    }
}
