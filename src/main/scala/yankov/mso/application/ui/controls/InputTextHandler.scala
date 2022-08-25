package yankov.mso.application.ui.controls

import javafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.control.{Control, TextField}
import scalafx.stage.Popup

import scala.annotation.tailrec

case class InputTextHandler(parent: Control,
                            onInput: String => Unit,
                            onDelete: Option[() => Unit]) {
  parent.setOnKeyTyped(x => handleKeyTyped(x))
  parent.setOnKeyReleased(x => handleKeyReleased(x))
  parent.focusedProperty().addListener((_, _, newValue) => handleFocusChanged(newValue))

  private val textField = new TextField {
    disable = true
  }

  private val popup = new Popup {
    content.add(textField)
  }

  private val inputText = new StringBuilder()

  @tailrec
  private def showPopup(offset: Double = 0.0): Unit = {
    val bounds = parent.localToScreen(parent.getBoundsInLocal)
    val x = bounds.getMinX
    val y = bounds.getMinY - offset
    popup.show(parent, x, y)
    if (offset == 0.0) showPopup(textField.getHeight)
  }

  private def close(): Unit = {
    inputText.setLength(0)
    popup.hide()
  }

  private def handleKeyTyped(event: KeyEvent): Unit = {
    def accept(char: Char): Boolean = char.isLetterOrDigit || char.isSpaceChar

    if (accept(event.getCharacter.toLowerCase.head)) {
      inputText.append(event.getCharacter.toLowerCase)
      textField.setText(inputText.toString)
      onInput(inputText.toString)
      showPopup()
    }
  }

  private def handleKeyReleased(event: KeyEvent): Unit = {
    if (event.getCode.equals(KeyCode.BACK_SPACE) && inputText.nonEmpty) {
      inputText.deleteCharAt(inputText.length - 1)
      textField.setText(inputText.toString)
      onInput(inputText.toString)
      showPopup()
    }
    else if (event.getCode.equals(KeyCode.DELETE)) onDelete.getOrElse(() => Unit).apply()
  }

  private def handleFocusChanged(focused: Boolean): Unit = if (!focused) close()
}
