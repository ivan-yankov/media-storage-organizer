package org.yankov.mso.application.ui.controls

import scalafx.scene.text.{Font, FontPosture, FontWeight}

object FontModel {

  trait Family {
    def cssRepresentation: String

    def fxName: String = cssRepresentation
  }

  case object Serif extends Family {
    override def cssRepresentation: String = "serif"
  }

  case object SansSerif extends Family {
    override def cssRepresentation: String = "sans-serif"
  }

  case object Cursive extends Family {
    override def cssRepresentation: String = "cursive"
  }

  case object Fantasy extends Family {
    override def cssRepresentation: String = "fantasy"
  }

  case object Monospace extends Family {
    override def cssRepresentation: String = "monospace"
  }

  trait Style {
    def cssRepresentation: String

    def fxFontPosture: FontPosture
  }

  case object NormalStyle extends Style {
    override def cssRepresentation: String = "normal"

    override def fxFontPosture: FontPosture = FontPosture.Regular
  }

  case object Italic extends Style {
    override def cssRepresentation: String = "italic"

    override def fxFontPosture: FontPosture = FontPosture.Italic
  }

  case object Oblique extends Style {
    override def cssRepresentation: String = "oblique"

    override def fxFontPosture: FontPosture = FontPosture.Italic
  }

  trait Weight {
    def cssRepresentation: String

    def fxFontWeight: FontWeight
  }

  case object NormalWeight extends Weight {
    override def cssRepresentation: String = "normal"

    override def fxFontWeight: FontWeight = FontWeight.Normal
  }

  case object Bold extends Weight {
    override def cssRepresentation: String = "bold"

    override def fxFontWeight: FontWeight = FontWeight.Bold
  }

  case object Bolder extends Weight {
    override def cssRepresentation: String = "bolder"

    override def fxFontWeight: FontWeight = FontWeight.ExtraBold
  }

  case object Lighter extends Weight {
    override def cssRepresentation: String = "lighter"

    override def fxFontWeight: FontWeight = FontWeight.Light
  }

  case class CustomFont(family: Family, style: Style, weight: Weight, size: Int) {
    def cssRepresentation: String =
      s"-fx-font-family:${family.cssRepresentation};-fx-font-weight:${weight.cssRepresentation};-fx-font-style:${style.cssRepresentation};-fx-font-size:${size.toString}px;"

    def toFxFont: Font = Font.font(family.fxName, weight.fxFontWeight, style.fxFontPosture, size)
  }

}
