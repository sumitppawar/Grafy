package com.sumit.graph

import play.api.libs.json.JsValue
import play.api.libs.json.JsArray
import com.sumit.util.GrafyConstant

object ResponseParser {
  
  def getError(jsValue: JsValue): (Int, String) = {
     if((jsValue \ "errors").as[JsArray].value.size > 0) {
        ((jsValue \ "errors").as[JsArray].value.size,(((jsValue \ "errors")(0).get) \ "message").as[String])
     }else {
       (0,GrafyConstant.EMPTY_STRING)
     }
  }
}