package com.sumit.connection

import com.sumitcommon.BaseGrafySpec
import play.api.libs.json.Json

class Neo4jPostJsonSpec extends BaseGrafySpec {
  
  "Neo4jPostJson json writes " should "produce correct json" in {
    val query_1 = "MATCH (node:Person {name:name}) RETUNR node"
    val parameter_1 = Map("name" -> "Sumit")
    val statement_1 = Statement(query_1, parameter_1)  
    val neo4jPostJson = Neo4jPostJson(List(statement_1))
    """{"statements":[{"statement":"MATCH (node:Person {name:name}) RETUNR node","parameters":{"name":"Sumit"},"resultDataContents":["row"],"includeStats":true}]}""" should equal(Json.toJson(neo4jPostJson).toString())
  }
  
}