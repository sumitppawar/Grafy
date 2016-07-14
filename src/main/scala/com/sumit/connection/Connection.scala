package com.sumit.connection

import play.api.libs.ws.{WSClient, WSRequest}
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json

/**
  * Created by sumit on 5/7/16.
  */
case class Connection(
                            protocol:String ,
                            serverName: String,
                            port:String ,
                            userName: String,
                            password: String
                          ) {

  def buildRequst( wSClient: WSClient): WSRequest = {
    val httpUrlForTransaction = s"$protocol://$serverName:$port/db/data/transaction/commit"
    
    val httpAuthHeader = "Basic " + GrafyUtils.encodeBase64(userName+":"+password)

    wSClient.url(httpUrlForTransaction)
        .withHeaders("Accept" -> "application/json; charset=UTF-8")
        .withHeaders("Content-Type" -> "application/json")
        .withHeaders(  "Authorization" -> httpAuthHeader)
  }
  
  def runCypherQuery(query: String)(implicit executionContext: ExecutionContext,wsClient: WSClient) = {
    val request = buildRequst(wsClient)
    
    for(wsResponse <- request.post(query)) yield {
      wsResponse.body
    }
    
  }
  
  
  def runCypherQuery(query: Cypher)(implicit executionContext: ExecutionContext,wsClient: WSClient) = {
    val request = buildRequst(wsClient)
    
    for(wsResponse <- request.post(Json.toJson(query))) yield {
      wsResponse.body
    }
    
  }
}

object Connection {
  def apply(userName: String,password: String): Connection = new Connection("http","localhost","7474",userName, password)
}