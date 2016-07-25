package com.sumit.graph


import org.scalatest.time.Millis
import org.scalatest.time.Span
import com.sumitcommon.BaseGrafySpec
import play.api.libs.json.Json
import com.sumit.connection.Connection
import org.scalatest.time.Seconds
import com.sumit.exception.GrafyException


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
    val result = executeCQL("MATCH")
    whenReady(result.failed) {ex =>
      ex shouldBe an[GrafyException]
    }
  }

}