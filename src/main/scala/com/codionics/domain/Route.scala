package com.codionics.domain

case class Route(httpVerb: String, path: String, method: String, params: Seq[Parameter]) {

  def toYamlString: String = {
    val verb = httpVerb.toLowerCase

    val paths          = path.split("/").filter(_.nonEmpty)
    val tag            = paths(1)
    val capitalizedTag = tag.capitalize

    // method is of the form com.intercax.syndeia.controllers.external.BitBucketController.getContainers
    val names = method.split("\\.")

    // so methodName becomes getContainers
    val methodName = names.last

    // and forSummary becomes ["get", "Containers"], so the second item is going to be the name of the domain object
    val forSummary = methodName.split("(?=\\p{Upper})")

    val forHeading = forSummary.map(_.toLowerCase)

    val summary =
      if (methodName.endsWith("s")) s"${forSummary.head.capitalize} $capitalizedTag ${forSummary.tail.mkString(" ")}"
      else s"${forSummary.head.capitalize} a $capitalizedTag ${forSummary.tail.mkString(" ")}"
    val heading = s"${forHeading.head}-$tag-${forHeading.tail.mkString("-")}"

    val response   = if (verb == "get") "'200'" else "'201'"
    val domain     = forSummary.tail.head.capitalize
    val domainName = if (domain.endsWith("s")) domain.dropRight(1) else domain
    val ref        = if (methodName.endsWith("s")) s"${domainName}SeqMessage" else s"${domainName}ObjMessage"

    val paramsString = params.map(_.toYamlString).mkString("\n")

    s"""
    $heading
      $verb:
        tags:
          - $capitalizedTag
        summary: $summary
        description: 
        operationId: $methodName
        parameters:
          $paramsString
        responses:
          $response
            description: s"$capitalizedTag $methodName"
            content:
              application/json:
                schema:
                  $ref: '#/components/schemas/$ref'
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
