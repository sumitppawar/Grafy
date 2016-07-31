package com.sumit.graph

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Promise

import com.sumit.connection.Connection

import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import com.sumit.exception.GrafyException

/**
 * Indicate Relationship Between  Nodes
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Relation(id: Long) extends CQL{

  /**
   * Delete Node
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return Boolean
   */
  def delete()(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Boolean] = {
    val promise = Promise[Boolean]
    val strCQL = s"START r=rel($id) DELETE r"
    runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(new GrafyException(error._2))
        } else {
          promise.success(true)
        }
      }
    }
    promise.future
  }
  
}

object Relation {
}