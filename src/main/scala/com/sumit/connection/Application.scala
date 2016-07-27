package com.sumit.connection

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient
import com.sumit.graph.Node
import com.sumit.graph.CQL


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
   // for (t <- CQL.runCypherQuery(cqlQuery)) yield println(t)
    
    //Pass parameter in map  CQL (preferred)
    val queryParametrised = "MATCH (you:Person)  where you.authCode={authCode} RETURN you"
    //Provide parameter in map ({authcode})
    val parameters = Map("authCode"->"jgfiuegkrp3fiugtgwfj")
    //build statement
    val statement = Statement(queryParametrised,parameters)
    val cypherObje = Neo4jPostJson(Seq(statement))
    val node = Node("2")
    //for (info <- node.getInfo(List("email","mobile"))) yield println(info)
    //for (info <- node.delete) yield println(info)
    var map = Map("email"-> "'sumit@nevitus.in'" )
    //for (info <- node.update(map)) yield println(info)
    
    var strCQL = "MATCH (node:Person) WHERE node.email='sumit@nevitus.com' return node.email"
    //for (info <-Node.find(strCQL)) yield println(info)
   //for(info <- Node("4").connect("Friend", "8", true, Map())) yield println(info)
/*    val label = "Person"
    val prop = Map("user_name" -> "ss3333333", "authcode" -> "ss", "email" -> "33ss33333@33u55u.com", "dob"->"03/07/1991")
    Node.create(label, prop).onComplete { x => println(x.get) }*/
   strCQL = "CREATE (node: Person {email:'sumit@test.com'}) RETURN ID(node),node.email"
   for(f <- CQL.executeCQL(strCQL)) yield print(f)
    
}
