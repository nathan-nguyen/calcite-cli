# calcite-cli

## Build and run

```bash
mvn package && java -jar target/calcite-cli-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Commands

### Create table
```sql
TABLE employees
COLUMNS employee_id, employee_name, employer_id
VALUES (
    1, Alex, 2
    2, Bob, 3
);
```

### SQL
```sql
SELECT * FROM employees;
```