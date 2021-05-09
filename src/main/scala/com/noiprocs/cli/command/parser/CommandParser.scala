package com.noiprocs.cli.command.parser

import com.noiprocs.cli.command.Command
import com.noiprocs.cli.command.parser.antlr4.{CalciteShellLexer, CalciteShellParser}
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree.ParseTreeWalker

object CommandParser {
  def parseCommand(command: String): Seq[Command] = {
    val commandCharArray = command.trim.toCharArray
    val stream = new ANTLRInputStream(commandCharArray, commandCharArray.length)
    val lexer = new CalciteShellLexer(stream)
    lexer.setTokenFactory(new CommonTokenFactory(true))

    val tokenStream = new UnbufferedTokenStream[CommonToken](lexer)

    val parser = new CalciteShellParser(tokenStream)
    parser.setTrimParseTree(true)

    parser.addErrorListener(
      new BaseErrorListener() {
        override def syntaxError(recognizer: Recognizer[_, _],
                                 offendingSymbol: Any, line: Int,
                                 charPositionInLine: Int,
                                 msg: String, e: RecognitionException): Unit = {
          throw new IllegalStateException(
            s"Failed to parse at line $line char $charPositionInLine due to $msg", e
          )
        }
      }
    )

    val commandContext = parser.command()
    val parseTreeWalker = new ParseTreeWalker
    val commandListener = new CommandListener

    parseTreeWalker.walk(commandListener, commandContext)
    commandListener.commandList
  }
}
