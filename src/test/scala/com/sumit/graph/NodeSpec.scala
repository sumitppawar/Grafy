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
  
  "def getInfo " should "should return empty map for invalid node" in {
    val node = Node("111")
    whenReady(node.getInfo(List("name"))) { map =>
      map should be (empty)
    }
  }
  
 "def getInfo " should "should return map for existing node" in {
      val node = Node(nodeId)
      whenReady(node.getInfo(List("email"))) { map =>
        map should not be(empty)
      }
  }
  
  "def getInfo " should "should return property value of node" in {
      val node = Node(nodeId)
      whenReady(node.getInfo(List("email"))) { map =>
       map.get("email").flatten should be (Some("test@grafy.com"))
      }
  }
 
 override def afterEach() = {
   Await.result(Node(nodeId).delete(),  30 second)
   ws.close()
 }
 
 override def beforeEach() = {
   val strCQL = "CREATE (node:Person {email:tets@grafy.com})  RETURN ID(node)"
   val futureResult  = Node.create("Person", Map("email"->"test@grafy.com"))
   futureResult map (str => nodeId = str)
   Await.result(futureResult,  30 second)
 }
}