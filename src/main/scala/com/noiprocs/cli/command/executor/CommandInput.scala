package com.noiprocs.cli.command.executor

import com.google.inject.Inject
import org.apache.commons.lang3.StringUtils
import scala.io.Source

trait CommandInputSource

case class FileInputSource(file: String) extends CommandInputSource {
  override def toString: String = file
}

case object StringInputSource extends CommandInputSource {
  override def toString: String = "<string>"
}

case class CommandInput(text: String,
                        source: CommandInputSource,
                        relativeLineNumber: Int) {
  override def toString: String = toString(0)

  def html: String = text
    .replace("\n", "<br/>")
    .replace(" ", "<span style=\"margin-left: 6px\"></span>")
    .replace("\t", "<span style=\"margin-left: 24px\"></span>") + "<br/>"

  def toString(lineNumber: Int): String = s"$text at $source: ${relativeLineNumber + lineNumber}"
}

class CommandReader @Inject()() {
  def removeComments(command: String): String = {
    val lines = command.split("\n")

    var commandStarted = false
    lines.filter(line => {
      if (commandStarted) true
      else if (line.trim.isEmpty || line.trim.startsWith("--")) false
      else {
        commandStarted = true

        true
      }
    }).mkString("\n")
  }

  private def createInput(content: String, source: CommandInputSource): Seq[CommandInput] = {
    CommandSplit.split(content)
      .filter(_.trim.nonEmpty)
      .map(removeComments)
      .filter(_.trim.nonEmpty)
      .map(command => {
        val previousContent = content.substring(0, content.indexOf(command))
        val relativeLineNum = StringUtils.countMatches(previousContent, "\n")
        CommandInput(command, source, relativeLineNum)
      })
  }

  def readString(command: String): Seq[CommandInput] = createInput(command, StringInputSource)

  def readFile(file: String): Seq[CommandInput] = {
    createInput(Source.fromFile(file).mkString, FileInputSource(file))
  }

  def withSource(command: String,
                 source: CommandInputSource): Seq[CommandInput] = createInput(command, source)
}

