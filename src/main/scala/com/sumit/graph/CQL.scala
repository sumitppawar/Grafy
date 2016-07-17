package com.sumit.graph

import com.sumit.connection.Cypher
import play.api.libs.ws.WSClient
import com.sumit.connection.Statement
import scala.concurrent.ExecutionContext
import com.sumit.connection.Connection
import play.api.libs.json.Json
import scala.concurrent.Future

object CQL {

  /**
   * @param query
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def runCypherQuery(query: String)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[String] = {
    val statement = Statement(query, Map())
    val cypherObje = Cypher(Seq(statement))
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
  def runCypherQuery(query: Cypher)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[String] = {
    val request = connection.buildRequst(wsClient)
    for (wsResponse <- request.post(Json.toJson(query))) yield wsResponse.body
  }
}