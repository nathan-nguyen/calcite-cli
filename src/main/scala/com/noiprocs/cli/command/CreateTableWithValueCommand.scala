package com.noiprocs.cli.command

import com.noiprocs.cli.sql.{InMemoryTable, SqlConnection}
import org.apache.log4j.LogManager

import scala.collection.JavaConverters.seqAsJavaListConverter

class CreateTableWithValueCommand(tableName: String,
                                  columnNameList: Seq[String],
                                  rowList: Seq[String]) extends Command {
  private lazy val logger = LogManager.getLogger(getClass.getName)

  override def execute(): Unit = {
    logger.info(s"Table name: $tableName")
    columnNameList.foreach(columnName => logger.info(s"Column: $columnName"))
    rowList.foreach(row => logger.info(s"Row: $row"))

    val calciteConnection = SqlConnection.getInstance.getCalciteConnection
    val valueArrayList = rowList.map(row => row.split(",")
      .map(_.trim)
      .asInstanceOf[Array[Object]])
      .asJava

    val table = new InMemoryTable(columnNameList.asJava, valueArrayList)
    calciteConnection.getRootSchema.add(tableName, table)

    logger.info(s"Table $tableName was created!")
    new SqlCommand(s"SELECT * FROM $tableName").execute()
  }
}
