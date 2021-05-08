package com.noiprocs.shell

import com.google.inject.Inject
import com.noiprocs.shell.command.parser.CommandParser
import org.apache.log4j.LogManager

class CommandExecutor @Inject()() {
  private lazy val Logger = LogManager.getLogger(getClass.getName)

  def execute(commandInput: CommandInput): Unit = {
    Logger.info(s"Executing ${commandInput.source} ${commandInput.relativeLineNumber}")
    CommandParser.parseCommand(commandInput.text)
  }

  def execute(commandInputSeq: Seq[CommandInput]): Unit = {
    commandInputSeq.foreach(execute)
  }

  def awaitQueryTermination(): Unit = {}
}

