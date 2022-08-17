package com.noiprocs.cli.sql;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.List;

public class InMemoryTable extends AbstractTable implements ScannableTable {
    private final List<String> columnNameList;
    private final List<Object[]> data;

    public InMemoryTable(List<String> columnNameList, List<Object[]> data) {
        this.columnNameList = columnNameList;
        this.data = data;
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return new AbstractEnumerable<Object[]>() {
            public Enumerator<Object[]> enumerator() {
                return new Enumerator<Object[]>() {
                    private int rowOffset = 0;

                    @Override
                    public Object[] current() {
                        return data.get(rowOffset - 1);
                    }

                    @Override
                    public boolean moveNext() {
                        return rowOffset++ < data.size();
                    }

                    @Override
                    public void reset() {
                        this.rowOffset = 0;
                    }

                    @Override
                    public void close() {
                    }
                };
            }
        };
    }

    @Override
    public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
        RelDataTypeFactory.Builder builder = typeFactory.builder();

        for (String columnName: columnNameList) {
            RelDataType relDataType = typeFactory.createTypeWithNullability(typeFactory.createSqlType(SqlTypeName.CHAR), true);
            builder.add(columnName, relDataType);
        }

        return builder.build();
    }
}