package com.sumit.graph

import com.sumit.common.BaseGrafySpec
import play.api.libs.json.Json

class ResponseParserSpec extends BaseGrafySpec {
  
  "def getError" should "return correct MESSAGE when response has error" in {
    val jsValue = Json.parse("""{"results":[],"errors":[{"code":"Neo.ClientError.Statement.InvalidSyntax","message":"Query cannot conclude with MATCH (must be RETURN or an update clause) (line 1, column 1 (offset: 0))\n\"Match n\"\n ^"}]}""")
    val tuple = ResponseParser.getError(jsValue);
    ("Query cannot conclude with MATCH (must be RETURN or an update clause) (line 1, column 1 (offset: 0))\n\"Match n\"\n ^") should equal(tuple._2)
  }

  "def getError" should "return correct ERROR COUNT one when response has error" in {
    val jsValue = Json.parse("""{"results":[],"errors":[{"code":"Neo.ClientError.Statement.InvalidSyntax","message":"Query cannot conclude with MATCH (must be RETURN or an update clause) (line 1, column 1 (offset: 0))\n\"Match n\"\n ^"}]}""")
    val tuple = ResponseParser.getError(jsValue);
    (1) should equal(tuple._1)
  }

  "def getError" should "return correct ERROR COUNT Zero when response is ok" in {
    val jsValue = Json.parse("""{"results":[],"errors":[]}""")
    val tuple = ResponseParser.getError(jsValue);
    (0) should equal(tuple._1)
  }
}