package com.sumit.util
import java.util.Base64
import play.api.libs.json.JsValue
import play.api.libs.json.JsNumber
import play.api.libs.json.JsBoolean

/**
  * Created by sumit on 5/7/16.
  */
object GrafyUtils {


  def encodeBase64(str: String = ""): String ={
    Base64.getEncoder.encodeToString(str.getBytes)
  }

  def decocdeBase64(str: String = ""): String ={
    Base64.getDecoder.decode(str.getBytes()).toString
  }
  
  def jsValueToScalaValue(jsValue: JsValue) = {
    if(jsValue.isInstanceOf[JsNumber]) jsValue.asOpt[Int]
    else if(jsValue.isInstanceOf[JsBoolean]) jsValue.asOpt[Boolean] 
    else  jsValue.asOpt[String] 
  }
}
