package com.sumit.graph

import scala.concurrent.Future

/**
 * Indicate Relationship Between  Nodes
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Relationship(id: String) {
  
  def getInfo(relPropertySelect: List[String]): Future[Map[String, String]] = {
    null
  }
}

object Relationship {
  
}