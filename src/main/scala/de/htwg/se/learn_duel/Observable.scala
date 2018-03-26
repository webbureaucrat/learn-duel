package de.htwg.se.learn_duel

trait Observable {
  private var observers: List[Observer] = List()
  def addObserver(observer: Observer): Unit = observers = observer :: observers
  def removeObserver(observer: Observer): Unit =
    observers = observers.filter(_ != observer)
  protected def notifyObservers(updateParam: UpdateData): Unit =
    observers.foreach(o => o.update(updateParam))
}
