# Grafy

* Grafy is build for Neo4j Graph db.
* Compatible with latest play Version (2.5.3).
* Built using sbt

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
##### create node and provide node id
```java
    val nodeLabel = "Person"
    //node property
    val property = Map("name" -> "Steve Jobs", "email" -> "stevejobs@apple.com", "mobile" -> "9403586847")
    Node.create(label, prop).onComplete { x => println(x.get)}   
```
