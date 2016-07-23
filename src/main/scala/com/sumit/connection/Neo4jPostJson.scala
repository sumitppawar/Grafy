package com.sumit.connection


import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * @author sumit
 *
 */
case class Neo4jPostJson(queries: Seq[Statement]) {
  
}

object Neo4jPostJson {
  implicit val neo4jPostJsonWrites: Writes[Neo4jPostJson] =   (__ \ "statements").write[Seq[Statement]].contramap {q => q.queries }
}