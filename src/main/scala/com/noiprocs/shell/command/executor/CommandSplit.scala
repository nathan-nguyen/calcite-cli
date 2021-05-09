package com.noiprocs.shell.command.executor

object CommandSplit {
  def split(source: String): List[String] = {
    source.split(";").toList
  }
}
