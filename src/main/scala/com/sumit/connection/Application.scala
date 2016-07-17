package com.sumit.connection

import com.sumit.graph.Node

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

    //Build Connection (default host is localhost, port is 7474, protocol is http
    implicit val connection = Connection("neo4j", "farmer")
    
    //Build connection with details (protocol: String,serverName: String,port:String,userName: String,password: String)
    //implicit val connectionWithDetails = Connection("http","localhost","7474","neo4j","farmer")
    
    //Execute CQL
    val cqlQuery = "MATCH (n:Person) Return ID(n)"
   // for (t <- connection.runCypherQuery(cqlQuery)) yield println(t)
    
    //Pass parameter in map  CQL (preferred)
    val queryParametrised = "MATCH (you:Person)  where you.authCode={authCode} RETURN you"
    //Provide parameter in map ({authcode})
    val parameters = Map("authCode"->"jgfiuegkrp3fiugtgwfj")
    //build statement
    val statement = Statement(queryParametrised,parameters)
    val cypherObje = Cypher(Seq(statement))
    val node = Node("Person","5")
    val resultFuture = node.getInfo(List("email","mobile"))
    
    for (t <- resultFuture) yield println(t)
    
/*    val label = "Person"
    val prop = Map("user_name" -> "ss3333333", "authcode" -> "ss", "email" -> "33ss33333@33u55u.com", "dob"->"03/07/1991")
    Node.create(label, prop).onComplete { x => println(x.get) }*/
}
