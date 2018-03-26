package de.htwg.se.learn_duel.view.impl.gui

import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.stage.Modality

class InfoPopup(titleText: String, text: String) extends Dialog[Unit] {
  title = titleText
  contentText = text

  dialogPane.value.getButtonTypes.add(ButtonType.OK)
  initModality(Modality.None)
}
