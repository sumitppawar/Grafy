package com.sumit.common

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient
import com.sumit.connection.Connection
import com.sumit.graph.CQL
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.ParallelTestExecution
import org.scalatest.time.Millis
import org.scalatest.time.Seconds
import org.scalatest.time.Span
import com.sumit.util.GrafyConstant

/**
 * @author sumit
 *
 */
trait BaseGrafySpec extends FlatSpec with CQL with BeforeAndAfterEach with BeforeAndAfterAll with Matchers with ScalaFutures with ParallelTestExecution  {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ws = AhcWSClient()
  
  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
  implicit val connection = Connection("neo4j", "farmer")
  implicit val defaultPatience = PatienceConfig(timeout = Span(30, Seconds), interval = Span(15, Millis))
  
  val createQuery = "CREATE (node:Person {email:'sumit@nevitus.com'}) return node.email";
  
  override def beforeAll() {
    
  }
  override def afterAll()  {
  }
  
  override def afterEach() {
    runCypherQuery("MATCH (node:Person {email:'sumit@nevitus.com'}) DELETE node")
    ws.close()
  }
  
}