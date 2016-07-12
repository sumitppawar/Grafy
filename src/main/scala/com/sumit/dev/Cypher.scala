package com.sumit.dev


import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Cypher(queries: Seq[Statement]) {
  
}

object Cypher {
  implicit val cypherWrites: Writes[Cypher] =   (__ \ "statements").write[Seq[Statement]].contramap {q => q.queries }
}