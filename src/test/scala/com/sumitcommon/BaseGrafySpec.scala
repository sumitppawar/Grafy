package com.sumitcommon

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient
import com.sumit.connection.Connection

trait BaseGrafySpec extends FlatSpec with BeforeAndAfterEach with BeforeAndAfterAll with Matchers {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ws = AhcWSClient()
  
  implicit val connection = Connection("neo4j", "farmer")
  
  override def afterAll()  {
    ws.close()
  }
  
}