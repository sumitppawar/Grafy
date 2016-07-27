package com.sumit.graph


import org.scalatest.time.Millis
import org.scalatest.time.Span
import play.api.libs.json.Json
import com.sumit.connection.Connection
import org.scalatest.time.Seconds
import com.sumit.exception.GrafyException
import com.sumit.common.BaseGrafySpec


/**
 * @author sumit
 *
 */
class CQLSpec extends BaseGrafySpec {

  "def runCypherQuery(query: String)" should " should return within 1 second" in {
    val result = runCypherQuery(createQuery)
    assert(result.isReadyWithin(Span(1000, Millis)))
  }

  "def runCypherQuery(query: Neo4jPostJson)" should "return response" in {
     val result = runCypherQuery(createQuery)
     whenReady(result) {s =>
       s should not equal("")
     }
  }
  
  "def executeCQL(strCQL: String)"  should "return failed response for Invalid query" in {
    val result = CQL.executeCQL("MATCH")
    whenReady(result.failed) {ex =>
      ex shouldBe an[GrafyException]
    }
  }

  "def executeCQL(strCQL: String)" should "return valid  property value for single match" in {
    val result = CQL.executeCQL("CREATE (node: Person {email:'sumitppawar@test.com',isAwesome:true}) RETURN ID(node),node.email,node.isAwesome")
    whenReady(result) { list =>
      val map = list(0)
      val isAwesome = map.get("node.isAwesome").flatten
      isAwesome should be(Some(true))
      CQL.executeCQL("MATCH (node: Person {email:'sumitppawar@test.com'}) DELETE node")
    }
  }
}