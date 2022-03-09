package com.codionics.parser

case class Route(httpVerb: String, path: String, method: String, params: Map[String, String])

object Route {

  def apply(httpVerb: String, path: String, method: String, parameters: Seq[String]): Route = {
    val paramsMap = parameters.map { param =>
      val Seq(paramName, paramType) = param.replace(",", "").trim().split(":").toSeq
      (paramName.trim(), paramType.trim())
    }.toMap
    
    Route(httpVerb, path, method, paramsMap)
  }
}