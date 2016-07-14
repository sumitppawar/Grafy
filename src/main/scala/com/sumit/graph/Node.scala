package com.sumit.graph

import scala.concurrent.Future

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
  
  def create(label: String, properties: Map[String, String]): Future[Option[String]] = {
    null
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