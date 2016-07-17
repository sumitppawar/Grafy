# Grafy
#### Project Settings 
1. Download Neo4j [Download](https://neo4j.com/download/)
2. Install sbt [Instruction](http://www.scala-sbt.org/0.13/docs/Setup.html)
3. Make eclipse project 
..1. Got Grafy dir and run following in terminal 
```
sumit@sumeet:~/workspace/Grafy$ sbt
> eclipse
```

#### How To Use
##### Build Connection
```java
//Execution Context
implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
//Wsclient
implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
implicit val ws = AhcWSClient()
//Build Connection with only user name and password
implicit val connection = Connection("neo4j", "farmer")
//Build connection with details (protocol: String,serverName: String,port:String,userName: String,password: String)
implicit val connectionWithDetails = Connection("http","localhost","7474","neo4j","farmer")
```
##### Execute CQL 
```java 
val cqlQuery = "MATCH (n:Person) Return ID(n)"
for (t <- connection.runCypherQuery(cqlQuery)) yield println(t)
```
##### Parameterized CQL  this is recommended by Neo4j
```java 
val queryParametrised = "MATCH (you:Person)  where you.authCode={authCode} RETURN you"
//Provide parameter in map ({authcode})
val parameters = Map("authCode"->"jgfiuegkrp3fiugtgwfj")
//build statement
val statement = Statement(queryParametrised,parameters)
val cypherObje = Cypher(Seq(statement))
for (t <- connection.runCypherQuery(cypherObje)) yield println(t)
```
##### Create node 
```java
val nodeLabel = "Person"
//node property
val property = Map("name" -> "Steve Jobs", "email" -> "stevejobs@apple.com", "mobile" -> "9403586847")
Node.create(label, prop).onComplete { x => println(x.get)}   //Returns Node Id
```
##### Get node info
```java
val node = Node("5") //Node(strlable:String, id: String)
val resultFuture = node.getInfo(List("email","mobile"))
for (info <- resultFuture) yield println(info) //Prints Map[key,value]
```
##### Update Node
```java
//Property to update
val map = Map("email"-> "'sumit@nevitus.in'")
Node("22").update(map) 
```
##### Delete Node

```java
//If a node is connected to another node, it can't delete
Node("22").delete
```
##### Find Node
```java
//Build find query
val strCQL = "MATCH (node:Person) WHERE node.friend='sumit' return node.name"
Node.find(strCQL) 
//return  List(Map(node.name -> Some(Ramdas)),Map(node.name -> Some(Prashant)),Map(node.name -> Some(Mohanish)))
//Note key of map is return of node
```