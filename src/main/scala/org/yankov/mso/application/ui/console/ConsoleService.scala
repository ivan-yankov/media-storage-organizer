package org.yankov.mso.application.ui.console

trait ConsoleService {
  def clear(): Unit

  def writeMessage(message: String): Unit

  def writeMessageWithTimestamp(message: String): Unit
}
