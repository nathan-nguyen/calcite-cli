package com.noiprocs.shell.command

import org.apache.log4j.LogManager

class CreateTableWithValueCommand(tableName: String,
                                  columnNameList: Seq[String],
                                  rowList: Seq[String]) extends Command {
  private lazy val Logger = LogManager.getLogger(getClass.getName)

  override def execute(): Unit = {
    Logger.info(s"Table name: $tableName")
    columnNameList.foreach(e => Logger.info(s"Column: $e"))
    rowList.foreach(e => Logger.info(s"Row: $e"))
  }
}
