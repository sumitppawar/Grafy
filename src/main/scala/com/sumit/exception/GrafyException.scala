package com.sumit.exception

/**
 * @author sumit
 *
 */
class GrafyException(message: String = null, cause: Throwable = null) extends RuntimeException(GrafyException.defaultMessage(message, cause), cause) {
  
}

object GrafyException {
  
  def defaultMessage(message: String, cause: Throwable) =
    if (message != null) message
    else if (cause != null) cause.toString()
    else null

}