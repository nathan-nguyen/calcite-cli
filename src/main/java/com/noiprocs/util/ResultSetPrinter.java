package com.noiprocs.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetPrinter {
    public static void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        // Print headers
        StringBuilder lineBreakSb = new StringBuilder("+");
        StringBuilder headerSb = new StringBuilder("|");

        for (int i = 1; i <= columnCount; ++i) {
            String columnName = resultSetMetaData.getColumnName(i);
            headerSb.append(" ").append(columnName).append("         |");

            while (lineBreakSb.length() < headerSb.length() - 1) lineBreakSb.append("-");
            lineBreakSb.append("+");
        }
        System.out.println(lineBreakSb);
        System.out.println(headerSb);
        System.out.println(lineBreakSb);

        // Print row values
        while (resultSet.next()) {
            StringBuilder valueSb = new StringBuilder("|");

            for (int i = 1; i <= columnCount; ++i) {
                String columnValue = resultSet.getString(i);
                valueSb.append(" ").append(columnValue);
                for (int j = valueSb.length(); valueSb.length() < lineBreakSb.length() && lineBreakSb.charAt(j) != '+'; ++j) {
                    valueSb.append(" ");
                }
                valueSb.append("|");
            }
            System.out.println(valueSb);
            System.out.println(lineBreakSb);
        }
    }
}
