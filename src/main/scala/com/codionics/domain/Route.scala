package com.codionics.domain

case class Route(httpVerb: String, path: String, method: String, params: Seq[Parameter]) {

  override def toString = {
    val verb = httpVerb.toLowerCase

    val paths = path.split("/").filter(_.nonEmpty)
    val tag   = paths(1).capitalize

    val names      = method.split("\\.")
    val methodName = names.last
    val forSummary = methodName.split("(?=\\p{Upper})")
    
    val summary    =
      if (methodName.endsWith("s")) s"${forSummary.head.capitalize} $tag ${forSummary.tail.mkString(" ")}"
      else s"${forSummary.head.capitalize} a $tag ${forSummary.tail.mkString(" ")}"

    val paramsString = params.map(_.toString).mkString("\n")

    s"""
      $verb:
        tags:
          - $tag
        summary: $summary
        description: 
        operationId: $methodName
        parameters:
          $paramsString
    """
  }
}

object Route {

  def create(httpVerb: String, path: String, method: String, parameters: Seq[String]): Route = {
    val params = parameters.map { param =>
      val Seq(paramName, paramType) = param.replace(",", "").trim().split(":").toSeq

      val name     = paramName.trim()
      val pType    = paramType.trim().toLowerCase()
      val location = if (path.contains(name)) ParamLocation.PathParam else ParamLocation.QueryParam
      Parameter(name, pType, "", location)
    }

    Route(httpVerb, path, method, params)
  }
}
