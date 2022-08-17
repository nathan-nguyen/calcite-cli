package com.noiprocs.cli.command;

import com.noiprocs.cli.sql.SqlConnection;
import com.noiprocs.util.ResultSetPrinter;

import java.sql.ResultSet;
import java.sql.Statement;

public class SqlCommand implements Command {
    private final String query;

    public SqlCommand(String query) {
        this.query = query;
    }

    @Override
    public void execute() {
        try {
            Statement statement = SqlConnection.getInstance().getStatement();

            ResultSet resultSet = statement.executeQuery(query);
            ResultSetPrinter.print(resultSet);
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
