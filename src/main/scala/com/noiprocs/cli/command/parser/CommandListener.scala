package com.noiprocs.cli.command.parser

import com.noiprocs.cli.command.{Command, CreateTableWithValueCommand}
import com.noiprocs.cli.command.parser.antlr4.{CalciteShellListener, CalciteShellParser}
import com.noiprocs.cli.command.parser.antlr4.CalciteShellParser._
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.{ErrorNode, TerminalNode}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class CommandListener extends CalciteShellListener {
  val commandList: mutable.ListBuffer[Command] = ListBuffer[Command]()

  /**
   * Enter a parse tree produced by {@link CalciteShellParser#   createTableWithValueCommand}.
   *
   * @param ctx the parse tree
   */
  override def enterCreateTableWithValueCommand(ctx: CreateTableWithValueCommandContext): Unit = {
    val tableName = ctx.tableName.getText
    val columnNameList = mutable.ListBuffer[String]()

    val columnNameIterator = ctx.columnName().iterator()
    while (columnNameIterator.hasNext) {
      val columnName = columnNameIterator.next()
      columnNameList.append(columnName.getText)
    }

    val rowList = ctx.valueBlock
      .getText.trim
      .split("[\n()]")
      .filter(_.nonEmpty).toList

    commandList.append(new CreateTableWithValueCommand(tableName, columnNameList, rowList))
  }

  /**
   * Exit a parse tree produced by {@link CalciteShellParser# createTableWithValueCommand}.
   *
   * @param ctx the parse tree
   */
  override def exitCreateTableWithValueCommand(ctx: CreateTableWithValueCommandContext): Unit = {
  }

  /**
   * Enter a parse tree produced by {@link CalciteShellParser# command}.
   *
   * @param ctx the parse tree
   */
  override def enterCommand(ctx: CommandContext): Unit = {
  }

  /**
   * Exit a parse tree produced by {@link CalciteShellParser# command}.
   *
   * @param ctx the parse tree
   */
  override def exitCommand(ctx: CommandContext): Unit = {
  }

  /**
   * Enter a parse tree produced by {@link CalciteShellParser# columnName}.
   *
   * @param ctx the parse tree
   */
  override def enterColumnName(ctx: ColumnNameContext): Unit = {
  }

  /**
   * Exit a parse tree produced by {@link CalciteShellParser# columnName}.
   *
   * @param ctx the parse tree
   */
  override def exitColumnName(ctx: ColumnNameContext): Unit = {
  }

  override def visitTerminal(terminalNode: TerminalNode): Unit = {
  }

  override def visitErrorNode(errorNode: ErrorNode): Unit = {
  }

  override def enterEveryRule(parserRuleContext: ParserRuleContext): Unit = {
  }

  override def exitEveryRule(parserRuleContext: ParserRuleContext): Unit = {
  }
}
