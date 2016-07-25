package com.sumit.graph

import com.sumit.connection.Neo4jPostJson
import play.api.libs.ws.WSClient
import com.sumit.connection.Statement
import scala.concurrent.ExecutionContext
import com.sumit.connection.Connection
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.libs.json.JsArray
import scala.concurrent.Promise

trait CQL {

  /**
   * @param query
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  protected def runCypherQuery(query: String)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[String] = {
    val statement = Statement(query, Map())
    val cypherObje = Neo4jPostJson(Seq(statement))
    val request = connection.buildRequst(wsClient)
    runCypherQuery(cypherObje)
  }

  /**
   * @param query
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
   protected  def runCypherQuery(query: Neo4jPostJson)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[String] = {
    val request = connection.buildRequst(wsClient)
    for (wsResponse <- request.post(Json.toJson(query))) yield wsResponse.body
  }

  /**
   * Execute CQL Return List[Map[String,String]]
   * @param strCQL
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def executeCQL(strCQL: String)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[List[Map[String, Option[String]]]] = {
    val promise = Promise[List[Map[String, Option[String]]]]
    runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(throw new Exception(error._2))
        } else {
          val jsValu = Json.parse(strJson)
          val column = ((jsValu \ "results")(0).get \ "columns").as[List[String]]
          val results = ((jsValu \ "results")(0).get \ "data").as[JsArray].value
          val resultList = (for (result <- results) yield {
            (for ((key, value) <- (column zip (result \ "row").as[JsArray].value)) yield {
              Map(key -> value.asOpt[String])
            }).flatten.toMap
          }).toList
          promise.success(resultList)
        }
      }
    }
    promise.future
  }
}