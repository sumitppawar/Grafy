package com.sumit.graph

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Promise

import com.sumit.connection.Connection
import com.sumit.connection.Neo4jPostJson
import com.sumit.connection.Statement
import com.sumit.exception.GrafyException
import com.sumit.util.GrafyConstant
import com.sumit.util.GrafyUtils

import play.api.libs.json.JsArray
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

/**
 * This class indicate Node
 * Date 14-July-2016
 * @author sumit
 *
 */
case class Node(id: Long) extends CQL{

  /**
   * @param selectProperties
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def getInfo(selectProperties: List[String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Map[String, Option[Any]]] = {
    
    if(selectProperties.size == 0)  {
      val map: Map[String,Option[Any]] = Map()
      return Future.successful(map)
    }
    
    val promise = Promise[Map[String, Option[Any]]]
    var strCQL = s"MATCH (node) WHERE ID(node)=$id RETURN "
    selectProperties.foreach { property => (strCQL += s"node.$property,") }
    strCQL = strCQL.dropRight(1)
    runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(new GrafyException(error._2))
        } else {
          val results = (jsValu \ "results").as[JsArray]
          val result = results(0).get
          val data = (result \ "data").as[JsArray]
          if(data.value.size > 0) {
          val row = (data.value(0) \ "row").as[JsArray].value
          val finalMap = (for ((key, value) <- (selectProperties zip row)) yield Map(key -> GrafyUtils.jsValueToScalaValue(value))).flatten.toMap
          promise.success(finalMap)
          } else {
        	  promise.success(Map())
          }
        }
      }
    }
    promise.future
  } 
  
  /**
   * Connect Node 
   * @param relLabel
   * @param nodeId
   * @param from
   * @param relProperties
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return
   */
  def connect(relLabel: String, nodeId: Long, from: Boolean, relProperties: Map[String, String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Long] = {
    if(from)
      Node.connect(relLabel, this.id, nodeId, relProperties) 
      else 
        Node.connect(relLabel, nodeId,this.id, relProperties) 
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
    var strCQL = s"MATCH (node) WHERE ID(node)=$id "
    strCQL = if(properties.size > 0) {
               strCQL = s"$strCQL SET " 
               properties.foreach { case (key, value) => strCQL += s"node.$key={$key}," }
               strCQL.dropRight(1)
              }
              else {
            	  s"$strCQL RETURN ID(node)" 
              }
    
    val cypherObje = Neo4jPostJson(Seq(Statement(strCQL, properties)))
    runCypherQuery(cypherObje).onSuccess {
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

object Node extends CQL{
  
  /**
   * Create node with give properties
   * @param label (Node label)
   * @param properties (Property of node)
   * @return It returns Node id
   */
  def create(label: String, properties: Map[String, String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Long] = {
    var parameter = GrafyConstant.EMPTY_STRING
    properties.foreach { case(key,value) =>
      parameter += s"$key:{$key},";
    }
    parameter = if(properties.size > 0)parameter.dropRight(1) else parameter
    
    val query = s"CREATE (node:$label {$parameter}) RETURN ID(node)"
    val cypherObje = Neo4jPostJson(Seq(Statement(query, properties)))

    runCypherQuery(cypherObje).flatMap { strJson =>
      val jsValu = Json.parse(strJson)
      val results = (jsValu \ "results").as[JsArray]
      val error = ResponseParser.getError(jsValu)

      if (error._1 > 0) {
        Future.failed(new GrafyException(error._2))
      } else {
        val result = results(0).get
        val data = (result \ "data")(0).get
        val id = (data \ "row")(0).get
        Future.successful(GrafyUtils.jsValueToScalaValue(id)match{case Some(x:Long) => x})
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
  def find(strCQL: String)(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[List[Map[String, Option[Any]]]] = {
    val promise = Promise[List[Map[String, Option[Any]]]]
    runCypherQuery(strCQL).onSuccess {
      case (strJson) => {
        val jsValu = Json.parse(strJson)
        val error = ResponseParser.getError(jsValu)
        if (error._1 > 0) {
          promise.failure(new GrafyException(error._2))
        } else {
          val jsValu = Json.parse(strJson)
          val column = ((jsValu \ "results")(0).get \ "columns").as[List[String]]
          val results = ((jsValu \ "results")(0).get \ "data").as[JsArray].value
          val resultList = (for (result <- results) yield {
            (for ((key, value) <- (column zip (result \ "row").as[JsArray].value)) yield {
              Map(key -> GrafyUtils.jsValueToScalaValue(value))
            }).flatten.toMap
          }).toList

          promise.success(resultList)
        }
      }
    }
    promise.future
  }

  /**
   * @param relLabel
   * @param fromNodeId
   * @param toNodeId
   * @param relProperties
   * @param connection
   * @param executionContext
   * @param wsClient
   * @return Rel Id
   */
  def connect(relLabel: String, fromNodeId: Long, toNodeId: Long, relProperties: Map[String, String])(implicit connection: Connection, executionContext: ExecutionContext, wsClient: WSClient): Future[Long] = {
    var strTem: String = GrafyConstant.EMPTY_STRING
    relProperties.foreach { case (key, value) => strTem += s"$key:{$key}," }
    strTem = strTem.dropRight(1)

    var strCQL = s"MATCH (node_1) WHERE ID(node_1)=$fromNodeId  MATCH (node_2) WHERE ID(node_2)=$toNodeId CREATE "
    strCQL += s"(node_1)-[rel:$relLabel {$strTem}]->(node_2) RETURN ID(rel)"

    val cypherObje = Neo4jPostJson(Seq(Statement(strCQL, relProperties)))

    runCypherQuery(cypherObje).flatMap { strJson =>
      val jsValu = Json.parse(strJson)
      val results = (jsValu \ "results").as[JsArray]
      val error = ResponseParser.getError(jsValu)
      if (error._1 > 0) {
        Future.failed(new GrafyException(error._2))
      } else {
        val result = results(0).get
        val data = (result \ "data")(0).get
        val id = (data \ "row")(0).get
        Future.successful(GrafyUtils.jsValueToScalaValue(id)match{case Some(x:Long) => x})
      }
    }
  } 
  
}