package com.sumit.graph

import scala.concurrent.Await

import com.sumit.common.BaseGrafySpec
import com.sumit.util.GrafyConstant
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import com.sumit.exception.GrafyException
import scala.language.postfixOps

/**
 * @author sumit
 *
 */
class NodeSpec extends BaseGrafySpec {
  private var nodeId = 0l
  private var anotherNodeId = 0l
  private var relId = 0l
  
  "def getInfo " should "return empty map for invalid node" in {
    val node = Node(111)
    whenReady(node.getInfo(List("name"))) { map =>
      map should be (empty)
    }
  }

  "def getInfo " should "not fail if nothing is passed in info list" in {
    val node = Node(nodeId)
    whenReady(node.getInfo(List())) { map =>
      map should be(empty)
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

  "def connect " should "return rel node" in {
    val node = Node(nodeId)
    val futureResult = node.connect("Friend", anotherNodeId, true, Map("status" -> "awesome friend"))
    whenReady(futureResult) { r =>
      relId = r
      //r should be (r>0)
    }
  }

  "def connect " should "fail for invalid id" in {
    val node = Node(Int.MinValue)
    val futureResult = node.connect("Friend", Int.MinValue, true, Map("status" -> "awesome friend"))
    whenReady(futureResult.failed) { r =>
      r shouldBe an[Exception]
    }
  }
  

  "def delete" should "delete node" in {
    val futureResult = Node.create("Person", Map("email" -> "tets_delete_node@grafy.com"))
    val node = Node(Await.result(futureResult, 30 second))
    whenReady(node.delete()) { ex =>
      ex should be(true)
    }
  }
  
  
  "def update" should "update Node" in {
    val node = Node(nodeId)
    val map = Map("email" -> "test_update@grafy.com")
    whenReady(node.update(map)) {b =>
      val resultMap = Await.result(Node(nodeId).getInfo(List("email")),  30 second)
      val email = resultMap.get("email").flatten
      email should be (Some("test_update@grafy.com"))
    }
  }

  "def update" should "not fail for node with nothing to update" in {
    val node = Node(nodeId)
    val map: Map[String,String]= Map()
    whenReady(node.update(map)) { b =>
      val resultMap = Await.result(Node(nodeId).getInfo(List("email")), 30 second)
      val email = resultMap.get("email").flatten
      email should be(Some("test_1@grafy.com"))
    }
  }
  
  "def create" should " create node with given properties" in {
    val nodeProperties = Map("email" -> "test_create_node@grafy.com")
    val nodeLabel = "Person"
    whenReady(Node.create(nodeLabel, nodeProperties)){ id => 
      val resultMap = Await.result(Node(id).getInfo(List("email")),  30 second)
      Await.result(Node(id).delete(),  30 second)
      
      resultMap.get("email").flatten should be (Some("test_create_node@grafy.com"))
    }
  }
  
  "def create" should " create node with no property" in {
    val nodeProperties: Map[String, String] = Map()
    val nodeLabel = "Person"
    whenReady(Node.create(nodeLabel, nodeProperties)) { id =>
      val resultMap = Await.result(Node(id).getInfo(List("email")), 30 second)
      Await.result(Node(id).delete(), 30 second)

      resultMap.get("email").flatten should be (None)
    }
  }
  
  "def find(strCQL: String)" should " find node specified in CQL" in {
    val strCQL = s"Match (node: Person {email:'test_1@grafy.com'}) RETURN ID(node)"
    whenReady(Node.find(strCQL)){ list => 
      val map = list(0)
      val optId = map.get("ID(node)").flatten
      optId should be (Some(nodeId))
    }
  }
  
  
 override def afterEach() = {
   if (relId != 0l) Await.result(Relation(relId).delete(),  30 second)
   Await.result(Node(nodeId).delete(),  30 second)
   Await.result(Node(anotherNodeId).delete(),  30 second)
   ws.close()
 }
 
 override def beforeEach() = {
   var futureResult  = Node.create("Person", Map("email"->"test_1@grafy.com"))
   futureResult map (str => nodeId = str)
   Await.result(futureResult,  30 second)
   
   futureResult  = Node.create("Person", Map("email"->"test_2@grafy.com"))
   futureResult map (str => anotherNodeId = str)
   Await.result(futureResult,  30 second)
 }
}