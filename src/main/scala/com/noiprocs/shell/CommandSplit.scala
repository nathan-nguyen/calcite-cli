package com.noiprocs.shell

object CommandSplit {
  def split(source: String): List[String] = {
    source.split(";").toList
  }
}
