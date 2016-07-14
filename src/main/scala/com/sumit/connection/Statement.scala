package com.sumit.connection

import play.api.libs.json._
import play.api.libs.functional.syntax._



/**
 * @author sumit
 *
 */
case class Statement(query: String, parameter: Map[String, String],resultDataContents:List[String],includeStats:Boolean) {
}

object Statement {
  
  def apply(query: String, parameter: Map[String, String]) = {
    val resultDataContents = List("row","graph")
    val includeStats = true;
    new Statement(query,parameter,resultDataContents,includeStats)
  }
  
  def apply(query: String) = {
    val parameter: Map[String, String] = Map.empty
    val resultDataContents = List("row","graph")
    val includeStats = true;
    new Statement(query,parameter,resultDataContents,includeStats)
  }
  
  implicit val queryWrites: Writes[Statement] = (
  (JsPath \ "statement").write[String] and
  (JsPath \ "parameters").write[Map[String,String]] and
  (JsPath \ "resultDataContents").write[List[String]] and
  (JsPath \ "includeStats").write[Boolean]
  )(unlift(Statement.unapply))
}


/*
{

{
  "statement": "MATCH (p:Person {authcode:\"abcdefgh\"}) SET p.mobile = \"7030700173\" return p",
  "parameter":{}
  "resultDataContents": [
    "row",
    "graph"
  ],
  "includeStats": true
}

*/