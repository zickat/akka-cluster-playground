package com.zickat.cluster.playground.node.processor

import akka.actor.{Actor, ActorRef, Props}
import ProcessorFibonacci.{Compute, ComputeNumber}

object Processor {

  sealed trait ProcessorMessage

  case class ComputeFibonacci(n: Int) extends ProcessorMessage

  def props(nodeId: String) = Props(new Processor(nodeId))
}

class Processor(nodeId: String) extends Actor {
  import Processor._

  val fibonacciProcessor: ActorRef = context.actorOf(ProcessorFibonacci.props(nodeId), "fibonacci")

  println(fibonacciProcessor)

  override def receive: Receive = {
    case ComputeFibonacci(value) => {
      val replyTo = sender()
      fibonacciProcessor ! Compute(value, replyTo)
    }
    case other: Int => {
      val replyTo = sender()
      fibonacciProcessor ! ComputeNumber(other, replyTo)
    }
  }
}
