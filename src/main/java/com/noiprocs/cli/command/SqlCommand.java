package com.noiprocs.cli.command;

import com.noiprocs.util.ResultSetPrinter;
import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class SqlCommand implements Command {
    private final String query;

    public SqlCommand(String query) {
        this.query = query;
    }

    @Override
    public void execute() {
        try {
            Class.forName("org.apache.calcite.jdbc.Driver");

            Properties info = new Properties();
            info.setProperty("lex", "JAVA");

            Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

            Statement statement = calciteConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetPrinter.print(resultSet);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
