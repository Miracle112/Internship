package com.example.internship;

import java.sql.*;

class DBHandler {
    Connection dbConnection;
    Statement statement;
    ResultSet resultSet;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://127.0.0.1:3306/practice"  + "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

       // dbConnection = DriverManager.getConnection(connectionString, "root", "carrbeat"); Миши
        dbConnection = DriverManager.getConnection(connectionString, "root", "Robbit50!");
        return dbConnection;
    }

    public ResultSet querry(String querry) {
        try{
            Statement statement = getDbConnection().createStatement();
            resultSet = statement.executeQuery(querry);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

}
