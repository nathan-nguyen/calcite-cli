package com.noiprocs.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetPrinter {
    public static void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        boolean printHeader = false;
        StringBuilder lbsb = new StringBuilder("+");

        while (resultSet.next()) {
            StringBuilder vsb = new StringBuilder("|");

            if (!printHeader) {
                printHeader = true;
                StringBuilder headerSb = new StringBuilder("|");

                for (int i = 1; i <= columnCount; ++i) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    headerSb.append(" ").append(columnName).append("         |");

                    while (lbsb.length() < headerSb.length() - 1) lbsb.append("-");
                    lbsb.append("+");
                }
                System.out.println(lbsb);
                System.out.println(headerSb);
                System.out.println(lbsb);
            }

            for (int i = 1; i <= columnCount; ++i) {
                String columnValue = resultSet.getString(i);
                vsb.append(" ").append(columnValue);
                for (int j = vsb.length(); vsb.length() < lbsb.length() && lbsb.charAt(j) != '+'; ++j) {
                    vsb.append(" ");
                }
                vsb.append("|");
            }
            System.out.println(vsb);
        }
        System.out.println(lbsb);
    }
}
