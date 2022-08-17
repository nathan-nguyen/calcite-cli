package com.noiprocs.cli.sql;

import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class SqlConnection {
    private static SqlConnection instance;

    private Connection connection;
    private CalciteConnection calciteConnection;
    private Statement statement;

    public static SqlConnection getInstance() {
        if (instance == null) {
            instance = new SqlConnection();
        }
        return instance;
    }

    private SqlConnection() {
        try {
            Class.forName("org.apache.calcite.jdbc.Driver");

            Properties info = new Properties();
            info.setProperty("lex", "JAVA");

            connection = DriverManager.getConnection("jdbc:calcite:", info);
            calciteConnection = connection.unwrap(CalciteConnection.class);
            statement = calciteConnection.createStatement();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            statement.close();
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CalciteConnection getCalciteConnection() {
        return calciteConnection;
    }

    public Statement getStatement() {
        return statement;
    }
}
