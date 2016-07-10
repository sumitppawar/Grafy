package com.sumit.dev

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

object Application extends App {
    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val ws = AhcWSClient()

    val connection = Connection("neo4j", "farmer")

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
