package yankov.mso.application.ui.console

import scalafx.scene.control.TextArea
import scalafx.scene.layout.StackPane

import java.time.LocalDateTime

object ApplicationConsole extends ConsoleService {
  private val textArea = new TextArea {
    prefRowCount = 5
    editable = false
  }

  val container: StackPane = new StackPane {
    children = List(textArea)
  }

  override def clear(): Unit = textArea.clear()

  override def writeMessage(message: String): Unit = {
    textArea.appendText(message)
    textArea.appendText(System.lineSeparator())
  }

  override def writeMessageWithTimestamp(message: String): Unit = writeMessage(produceTimestamp + message)

  private def produceTimestamp: String = {
    val now = LocalDateTime.now()
    "[%02d.%02d.%04d, %02d:%02d:%02d] ".format(
      now.getDayOfMonth,
      now.getMonthValue,
      now.getYear,
      now.getHour,
      now.getMinute,
      now.getSecond)
  }
}
