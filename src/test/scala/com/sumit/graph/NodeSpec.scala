package com.sumit.graph

import scala.concurrent.Await

import com.sumit.common.BaseGrafySpec
import com.sumit.util.GrafyConstant
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
/**
 * @author sumit
 *
 */
class NodeSpec extends BaseGrafySpec {
  private var nodeId = GrafyConstant.EMPTY_STRING
  private var anotherNodeId = GrafyConstant.EMPTY_STRING
  private var relId = GrafyConstant.EMPTY_STRING
  
  "def getInfo " should "return empty map for invalid node" in {
    val node = Node("111")
    whenReady(node.getInfo(List("name"))) { map =>
      map should be (empty)
    }
  }
  
 "def getInfo " should "return map for existing node" in {
      val node = Node(nodeId)
      whenReady(node.getInfo(List("email"))) { map =>
        map should not be(empty)
      }
  }
  
  "def getInfo " should "return property value of node" in {
      val node = Node(nodeId)
      whenReady(node.getInfo(List("email"))) { map =>
       map.get("email").flatten should be (Some("test_1@grafy.com"))
      }
  }

  "def connect " should "return rel id" in {
    val node = Node(nodeId)
    val futureResult = node.connect("Friend", anotherNodeId, true, Map("status" -> "awesome friend"))
    whenReady(futureResult) { r =>
      relId = r
      r should not be (empty)
    }
  }
  
  
 override def afterEach() = {
   if (!relId.isEmpty()) Await.result(Relation(relId).delete(),  30 second)
   Await.result(Node(nodeId).delete(),  30 second)
   Await.result(Node(anotherNodeId).delete(),  30 second)
   ws.close()
 }
 
 override def beforeEach() = {
   var strCQL = "CREATE (node:Person {email:tets_1@grafy.com})  RETURN ID(node)"
   var futureResult  = Node.create("Person", Map("email"->"test_1@grafy.com"))
   futureResult map (str => nodeId = str)
   Await.result(futureResult,  30 second)
   
   strCQL = "CREATE (node:Person {email:tets_2@grafy.com})  RETURN ID(node)"
   futureResult  = Node.create("Person", Map("email"->"test_2@grafy.com"))
   futureResult map (str => anotherNodeId = str)
   Await.result(futureResult,  30 second)
 }
}