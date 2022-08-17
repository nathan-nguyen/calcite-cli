package com.noiprocs.cli

import com.google.inject.{Guice, Inject}
import com.noiprocs.cli.command.executor.{CommandExecutor, CommandInputSource, CommandReader}
import com.noiprocs.cli.sql.SqlConnection
import org.jline.reader.{LineReader, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.{Terminal, TerminalBuilder}

class CalciteShell @Inject() (commandReader: CommandReader, commandExecutor: CommandExecutor) {
  val input: StringBuffer = new StringBuffer()

  var prompt: String = CalciteShell.PROMPT_TEXT

  class TerminalSignalHandler extends Terminal.SignalHandler {
    override def handle(signal: Terminal.Signal): Unit = {
      println()
      quit()
    }
  }

  lazy val terminal: Terminal = {
    val term = TerminalBuilder
      .builder()
      .build()

    term.handle(Terminal.Signal.INT, new TerminalSignalHandler)
    term
  }

  lazy val lineReader: LineReader = {
    val lineReader = LineReaderBuilder
      .builder()
      .terminal(terminal)
      //      .completer(new AggregateCompleter(catalogCompleter, commandCompleter))
      .build()

    lineReader.setOpt(LineReader.Option.CASE_INSENSITIVE)
    lineReader.setOpt(LineReader.Option.CASE_INSENSITIVE_SEARCH)
    lineReader
  }

  def run(): Unit = {
    inputLoop()
  }

  def inputLoop(): Unit = {
    while (true) try {
      val line = lineReader.readLine(prompt)

      // Check for special command if this is the first line
      if (input.length == 0) {
        var command = line.trim

        if (command.endsWith(";")) command = command.substring(0, command.length - 1)
        command match {
          case "exit" | "quit" => quit()
          case _ =>
        }
      }

      if (line.trim.nonEmpty || input.length() > 0) {
        input.append(line)
        input.append("\n")

        prompt = null
        if (line.trim.endsWith(";")) try {
          val commandInputSeq = commandReader.withSource(input.toString, CalciteShell.SHELL_SOURCE)

          println()
          commandExecutor.execute(commandInputSeq)
          commandExecutor.awaitQueryTermination()
          println()
        } catch {
          case ne: NoClassDefFoundError => throw ne
          case e: Throwable => printStackTrace(e)
        } finally {
          // Clear input
          input.setLength(0)
          prompt = CalciteShell.PROMPT_TEXT
        }
      }
    } catch {
      case _: UserInterruptException =>
        if (lineReader.getBuffer.length() > 0) {
          // Clear input
          input.setLength(0)
          prompt = CalciteShell.PROMPT_TEXT
          lineReader.getBuffer.clear()
        } else {
          quit()
        }

      case e: Throwable =>
        printStackTrace(e)
        quit()
    }
  }

  def quit(): Unit = {
    SqlConnection.getInstance().close()

    System.exit(0)
  }

  def printStackTrace(e: Throwable): Unit = {
    e.printStackTrace()
    println()
  }
}


object CalciteShell {
  case object SHELL_SOURCE extends CommandInputSource {
    override def toString: String = "<CLI>"
  }

  final val PROMPT_TEXT = "calcite-shell> "

  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new CalciteModule())
    val calciteShell = injector.getInstance(classOf[CalciteShell])

    println(Calcite.appBanner)
    calciteShell.run()
  }
}
