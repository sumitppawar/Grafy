package com.sumit.graph

import com.sumitcommon.BaseGrafySpec
import com.sumit.util.GrafyConstant


/**
 * @author sumit
 *
 */
class NodeSpec extends BaseGrafySpec {
  
  "def getInfo " should "should return empty map for invalid node" in {
    val node = Node("111")
    whenReady(node.getInfo(List("name"))) { map =>
      map should be (empty)
    }
  }
  
 "def getInfo " should "should return map for existing node" in {
      val node = Node("6")
      whenReady(node.getInfo(List("email"))) { map =>
        println(map.toString())
        map should not be(empty)
      }
  }
  
}