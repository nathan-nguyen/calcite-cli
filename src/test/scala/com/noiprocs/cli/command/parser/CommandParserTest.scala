package com.noiprocs.cli.command.parser

import org.scalatest.FunSuite

class CommandParserTest extends FunSuite {
  test("CommandParser.parseCommand - 00") {
    CommandParser.parseCommand(
      """
        |TABLE employees
        |COLUMNS employee_id, employee_name, employer_id
        |VALUES (
        |    1, Alex, 2
        |    2, Bob, 3
        |)""".stripMargin.trim
    ).foreach(_.execute())
  }
}
