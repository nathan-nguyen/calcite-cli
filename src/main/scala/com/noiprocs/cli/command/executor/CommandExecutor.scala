package com.noiprocs.cli.command.executor

import com.google.inject.Inject
import com.noiprocs.cli.command.SqlCommand
import com.noiprocs.cli.command.parser.CommandParser
import org.apache.log4j.LogManager

class CommandExecutor @Inject()() {
  private lazy val Logger = LogManager.getLogger(getClass.getName)

  def execute(commandInput: CommandInput): Unit = {
    Logger.info(s"Executing ${commandInput.source} ${commandInput.relativeLineNumber}")
    try {
      CommandParser.parseCommand(commandInput.text).foreach(_.execute())
    } catch {
      case e: Exception => new SqlCommand(commandInput.text).execute()
    }
  }

  def execute(commandInputSeq: Seq[CommandInput]): Unit = {
    commandInputSeq.foreach(execute)
  }

  def awaitQueryTermination(): Unit = {}
}

