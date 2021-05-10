package com.noiprocs.cli.command

import org.scalatest.FunSuite

class SqlCommandTest extends FunSuite {
  test("SqlCommand - 00") {
    val sql = "SELECT 1 AS a, 2 AS b, 3 AS c"
    new SqlCommand(sql).execute()
  }

  test("SqlCommand - 01") {
    val sql = "SELECT 1 AS ahahaha, 2 AS ehehehe, 3 AS ihihihi"
    new SqlCommand(sql).execute()
  }
}
