package com.noiprocs.shell.command.parser

import com.noiprocs.shell.command.parser.antlr4.{CalciteShellLexer, CalciteShellParser}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import org.apache.log4j.LogManager

object CommandParser {
  private lazy val Logger = LogManager.getLogger(getClass.getName)

  def parseCommand(command: String): Unit = {
    val commandCharArray = command.trim.toCharArray
    val stream = new ANTLRInputStream(commandCharArray, commandCharArray.length)
    val lexer = new CalciteShellLexer(stream)
    val tokens = new CommonTokenStream(lexer)

    val parser = new CalciteShellParser(tokens)

    val createTableWithValueCommandContext = parser.createTableWithValueCommand()
    Logger.info(s"Table name: ${createTableWithValueCommandContext.tableName.getText}")

    val columnNameIterator = createTableWithValueCommandContext.columnName().iterator()
    while (columnNameIterator.hasNext) {
      val columnName = columnNameIterator.next()
      Logger.info(s"Column: ${columnName.getText}")
    }

    createTableWithValueCommandContext.valueBlock
      .getText.trim
      .split("[\n()]")
      .filter(_.nonEmpty)
      .foreach(row => Logger.info("Row: " + row))
  }
}
