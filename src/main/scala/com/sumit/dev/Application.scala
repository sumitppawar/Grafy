package com.sumit.dev

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

/**
 * @author sumit
 * This starting point of Grafy
 * run by using "sbt run"
 */
object Application extends App {
  
    //Execution Context
    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
    
    //Wsclient
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val ws = AhcWSClient()

    //Build Connection 
    val connection = Connection("neo4j", "farmer")

    //Sample get Query
    val cypher = """{
			 "statements":[
                			 {
                  			 "statement":"MATCH (you:Person)  where you.authCode={authCode} RETURN you",
                  			 "parameters":{"authCode":"jgfiuegkrp3fiugtgwfj"}
                			 }
			             ]
			       }"""

    for (t <- connection.runCypherQuery(cypher)) yield println(t)
}
