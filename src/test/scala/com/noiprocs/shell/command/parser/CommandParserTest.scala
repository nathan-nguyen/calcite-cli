package com.noiprocs.shell.command.parser

import org.scalatest.FunSuite

class CommandParserTest extends FunSuite {
  test("CommandParser.parseCommand - 00") {
    val command =
      """
        |TABLE employee_table
        |COLUMNS employer_id, employee_id
        |VALUES (
        |1, 2
        |2, 3
        |)
        |""".stripMargin
    CommandParser.parseCommand(command.trim)
  }
}
