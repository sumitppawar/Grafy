package com.sumit.util
import java.util.Base64

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
}
