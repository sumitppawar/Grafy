package com.sumit.connection

import com.sumit.util.GrafyUtils
import com.sumit.common.BaseGrafySpec

class ConnectionSpec extends BaseGrafySpec {

  "def buildRequest" should "build Request with Authorization header " in {
    val authHeader = "Authorization"
    val authHeaderValue = "Basic " + GrafyUtils.encodeBase64(connection.userName + ":" + connection.password)
    (authHeaderValue) should equal(connection.buildRequst(ws).headers.get(authHeader).getOrElse(Seq(""))(0))
  }

  "def buildRequest" should "build Request with Accept header" in {
    ("application/json; charset=UTF-8") should equal(connection.buildRequst(ws).headers.get("Accept").getOrElse(Seq(""))(0))
  }

  "def buildRequest" should "build Request with Content-Type header" in {
    ("application/json") should equal(connection.buildRequst(ws).headers.get("Content-Type").getOrElse(Seq(""))(0))
  }
  
  "def buildRequest" should "should have correct request URL" in {
    val protocol = connection.protocol
    val serverName = connection.serverName
    val port = connection.port
    (s"$protocol://$serverName:$port/db/data/transaction/commit") should equal(connection.buildRequst(ws).url)
  }
}