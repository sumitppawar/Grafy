package com.sumit.connection

import com.sumit.common.BaseGrafySpec
import play.api.libs.json.Json

class StatementSpec extends BaseGrafySpec {
  
  "Statement writes" should "genrate correct josn for it" in {
    val query = "MATCH (node:Person {name:name}) RETUNR node"
    val parameter = Map("name" -> "Sumit")
    val statement = Statement(query, parameter)    
    """{"statement":"MATCH (node:Person {name:name}) RETUNR node","parameters":{"name":"Sumit"},"resultDataContents":["row"],"includeStats":true}"""  should equal (Json.toJson(statement).toString())
  }
  
  "Statement writes" should "genrate correct json when result data content is specified" in {
    val query = "MATCH (node:Person {name:name}) RETUNR node"
    val parameter = Map("name" -> "Sumit")
    val statement = Statement(query, parameter,List("graph","row"),false)
    """{"statement":"MATCH (node:Person {name:name}) RETUNR node","parameters":{"name":"Sumit"},"resultDataContents":["graph","row"],"includeStats":false}""" should equal(Json.toJson(statement).toString())
  }
  
}