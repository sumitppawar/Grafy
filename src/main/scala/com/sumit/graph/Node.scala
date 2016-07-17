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
import play.api.libs.json.JsValue

/**
 * This class indicate Node
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Node(id: String) {

  def getInfo(selectProperties: List[String])(implicit connection:Connection, executionContext: ExecutionContext,wsClient: WSClient): Future[Map[String, Option[String]]] = {
	 val promise = Promise[Map[String,Option[String]]] 
   var strCQL = s"MATCH (node) WHERE ID(node)=$id RETURN "
   selectProperties.foreach {property => (strCQL += s"node.$property,")}
   strCQL = strCQL.dropRight(1)
   CQL.runCypherQuery(strCQL).onSuccess {
     case(strJson) => {
    	 val jsValu = Json.parse(strJson)
       val error = ResponseParser.getError(jsValu)
      if (error._1 > 0) {
        promise.failure(throw new Exception(error._2))
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

  /**
   * Delete node if node does not connected to any other Node
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def delete()(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Boolean] = {
    val promise = Promise[Boolean]
    var strCQL = s"MATCH (node) WHERE ID(node)=$id DELETE node "
    CQL.runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(throw new Exception(error._2))
        } else {
          promise.success(true)
        }
      }
    }
    promise.future
  }

  /**
   * Update node 
   * @param properties
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def update(properties: Map[String, String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Boolean] = {
    val promise = Promise[Boolean]
    var strCQL = s"MATCH (node) WHERE ID(node)=$id SET "
    properties.foreach { case (key, value) => strCQL += s"node.$key=$value," }
    strCQL = strCQL.dropRight(1)
    CQL.runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(throw new Exception(error._2))
        } else {
          promise.success(true)
        }
      }
    }
    promise.future
  }
}

object Node {
  
  /**
   * Create node with give properties
   * @param label (Node label)
   * @param properties (Property of node)
   * @return It returns Node id
   */
  def create(label: String, properties: Map[String, String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[String] = {
    var parameter = GrafyConstant.EMPTY_STRING
    properties.keySet.foreach { key =>
      parameter += s"$key:{$key},";
    }
    parameter = parameter.dropRight(1)
    val query = s"CREATE (node:$label {$parameter}) RETURN ID(node)"
    val cypherObje = Cypher(Seq(Statement(query, properties)))

    CQL.runCypherQuery(cypherObje).flatMap { strJson =>
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

  /**
   * This method execute passed CQL and return List(Map)
   * Map contains key as selectable
   * Ex
   * Match (node:Person) Where node.email='sumit@nevitus.com' return node.mobile
   * return value is List(Map(node.mobile -> "54752574")
   * @param strCQL
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def find(strCQL: String)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[List[Map[String, Option[String]]]] = {
    val promise = Promise[List[Map[String, Option[String]]]]
    CQL.runCypherQuery(strCQL).onSuccess {
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
    
  def connect(relLabel: String,fromNodeId: String, toNodeId: String, relProperties: Map[String, String]): Future[String]= {
    null
  } 
  
}