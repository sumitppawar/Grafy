package com.sumit.graph

import scala.concurrent.Future
import com.sumit.util.GrafyConstant
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext
import com.sumit.connection.Connection
import com.sumit.connection.Cypher
import com.sumit.connection.Statement
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.JsArray

/**
 * This class indicate Node
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Node(id: String) {

  def getInfo(selectProperties: List[String]): Future[Map[String, String]] = {
    null
  } 
  
  def getRelated(relationLabel: String, nodePropertysSelect: List[String], relationPropertySelect: List[String]): Future[List[Map[String, String]]] = {
    null
  }
  
  def connect(relLabel: String,nodeId: String, from: Boolean, relProperties: Map[String, String]): Future[String] = {
    null
  } 
  
}

object Node {
    /**
   * Create node with give properties 
   * @param label (Node label)
   * @param properties (Property of node)
   * @return It returns Node id
 	 */

  def create(label: String, properties: Map[String, String])(implicit connection:Connection, executionContext: ExecutionContext,wsClient: WSClient): Future[String] = {
	  var parameter = GrafyConstant.EMPTY_STRING
	 
	  //Please find createNodePost.json and createNodeReponse.json for more Info
	  properties.keySet.foreach { key =>
	    parameter += s"$key:{$key},";
	  }
	  parameter = parameter.dropRight(1)
	  
    val query =  s"CREATE (node:$label {$parameter}) RETURN ID(node)" 
    val cypherObje = Cypher(Seq(Statement(query,properties)))
    val resultFuture = connection.runCypherQuery(cypherObje)
    
    resultFuture.flatMap { strJson =>
      val jsValu = Json.parse(strJson)
      val results = (jsValu \ "results").as[JsArray]
      
      if((jsValu \ "errors").as[JsArray].value.size > 0) {
       val message = (((jsValu \ "errors")(0).get) \ "message").as[String]
       Future.failed(throw new Exception(message))
      }else {
        val result = results(0).get
        val data = (result \ "data")(0).get
        val id = (data \ "row")(0).get
        Future.successful(id.toString())
      }
      
	  }
  }
  
  def delete(id: String): Future[Boolean] = {
    null
  }
  
  def update(id: String, properties: Map[String, String]):Future[Boolean] = {
    null
  }
  
  def find(label: String, whereClause: String,selectProperties:List[String]): Future[List[Map[String,String]]] = {
    null
  }
  
  def connect(relLabel: String,fromNodeId: String, toNodeId: String, relProperties: Map[String, String]): Future[String]= {
    null
  } 
  
  def getInfo(label: String,ids: List[String], selectProperties:List[String]): Future[List[Map[String,String]]] = {
    null
  }
  
}