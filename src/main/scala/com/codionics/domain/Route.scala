package com.codionics.domain

case class Route(httpVerb: String, path: String, method: String, params: Seq[Parameter])

object Route {

  def create(httpVerb: String, path: String, method: String, parameters: Seq[String]): Route = {
    val params = parameters.map { param =>
      val Seq(paramName, paramType) = param.replace(",", "").trim().split(":").toSeq

      val name = paramName.trim()
      val pType = paramType.trim().toLowerCase()
      val location = if (path.contains(name)) ParamLocation.PathParam else ParamLocation.QueryParam
      Parameter(name, pType, "", location)
    }
    
    Route(httpVerb, path, method, params)
  }
}