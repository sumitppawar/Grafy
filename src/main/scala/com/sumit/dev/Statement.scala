package com.sumit.dev

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Statement(query: String, parameter: Map[String, String]) {
}

object Statement {
  implicit val queryWrites: Writes[Statement] = (
  (JsPath \ "statement").write[String] and
  (JsPath \ "parameters").write[Map[String,String]]
  )(unlift(Statement.unapply))
}