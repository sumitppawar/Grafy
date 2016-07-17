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
import java.time.Year
import scala.concurrent.Promise
import play.api.libs.json.JsNull

/**
 * This class indicate Node
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Node(strlable:String, id: String) {

  def getInfo(selectProperties: List[String])(implicit connection:Connection, executionContext: ExecutionContext,wsClient: WSClient): Future[Map[String, Option[String]]] = {
	 val promise = Promise[Map[String,Option[String]]] 
   var strCQL = s"MATCH (node:$strlable) WHERE ID(node)=$id RETURN "
   selectProperties.foreach {property => (strCQL += s" node.$property,")}
   strCQL = strCQL.dropRight(1)
   connection.runCypherQuery(strCQL).onSuccess {
     case(strJson) => {
    	 val jsValu = Json.parse(strJson)
       val error = ResponseParser.getError(jsValu)
      if (error._1 > 0) {
        Future.failed(throw new Exception(error._2))
      } else {
       val results = (jsValu \ "results").as[JsArray]
       val result = results(0).get
       val data = (result \ "data")(0).get
       val row = (data \ "row").as[JsArray].value
       val finalMap = (for((key,value) <- (selectProperties zip row)) yield Map(key -> value.asOpt[String])).flatten.toMap
       promise.success(finalMap)
      }
     }
   }
   promise.future
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
    properties.keySet.foreach { key =>
      parameter += s"$key:{$key},";
    }
    parameter = parameter.dropRight(1)
    val query = s"CREATE (node:$label {$parameter}) RETURN ID(node)"
    val cypherObje = Cypher(Seq(Statement(query, properties)))

    connection.runCypherQuery(cypherObje).flatMap { strJson =>
      val jsValu = Json.parse(strJson)
      val results = (jsValu \ "results").as[JsArray]
      val error = ResponseParser.getError(jsValu)
      
      if (error._1 > 0) {
        Future.failed(throw new Exception(error._2))
      } else {
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
  
  def getInfo(label: String,ids: List[String], selectProperties:List[String]): Future[List[Map[String,Option[String]]]] = {
    null
  }
  
  
}