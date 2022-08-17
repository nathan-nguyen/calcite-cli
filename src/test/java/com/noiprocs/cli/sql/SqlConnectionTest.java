package com.noiprocs.cli.sql;

import com.noiprocs.util.ResultSetPrinter;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlConnectionTest {
    private static final Logger logger = LogManager.getLogger(SqlConnectionTest.class);

    @Test
    public void testAddTableToRootSchema() throws Exception {
        addTableToRootSchema();
        execute("SELECT * FROM employee_table");
        execute("SELECT * FROM employee_table");
    }

    public void execute(String query) throws Exception {
        Statement statement = SqlConnection.getInstance().getStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetPrinter.print(resultSet);
        resultSet.close();
    }

    public void addTableToRootSchema() {
        CalciteConnection calciteConnection = SqlConnection.getInstance().getCalciteConnection();
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        logger.info(rootSchema.getTableNames());

        List<String> columnName = new ArrayList<>();
        columnName.add("employer_id");
        columnName.add("employee_id");

        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{1, 2});
        data.add(new Object[]{2, 3});

        Table table = new InMemoryTable(columnName, data);
        rootSchema.add("employee_table", table);
    }

    @Test
    public void testReflectiveSchema() throws Exception {
        CalciteConnection calciteConnection = SqlConnection.getInstance().getCalciteConnection();
        SchemaPlus rootSchema = calciteConnection.getRootSchema();

        Schema schema = new ReflectiveSchema(new HrSchema());
        rootSchema.add("hr", schema);

        Statement statement = calciteConnection.createStatement();

        ResultSet resultSet1 = statement.executeQuery(
                "SELECT employees.department_id, MIN(employee_id) AS min_employee_id\n" +
                        "FROM hr.employees\n" +
                        "INNER JOIN hr.departments\n" +
                        "ON employees.department_id = departments.department_id\n" +
                        "GROUP BY employees.department_id\n" +
                        "HAVING COUNT(*) > 1"
        );
        ResultSetPrinter.print(resultSet1);
        resultSet1.close();

        ResultSet resultSet2 = statement.executeQuery("SELECT * FROM hr.employees");
        ResultSetPrinter.print(resultSet2);
        resultSet2.close();

        ResultSet resultSet3 = statement.executeQuery("SELECT * FROM hr.employees WHERE false");
        ResultSetPrinter.print(resultSet3);
        resultSet3.close();
    }

    public static class Employee {
        public int employee_id;
        public int department_id;
    }

    public static class Department {
        public int department_id;
    }

    public static class HrSchema {
        public final Employee[] employees = new Employee[]{new Employee(), new Employee()};
        public final Department[] departments = new Department[]{new Department()};
    }
}
