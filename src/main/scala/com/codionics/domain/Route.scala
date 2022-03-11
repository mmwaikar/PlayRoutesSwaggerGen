package com.codionics.domain

case class Route(httpVerb: String, path: String, method: String, params: Seq[Parameter]) {

  def getResponse: String = {
    if (httpVerb.toLowerCase == "get") "'200'" else "'201'"
  }

  def toYamlString: String = {
    val routeHelper = Route.getRouteHelper(httpVerb, path, method)
    val verb        = httpVerb.toLowerCase

    val summary     = routeHelper.getSummary
    val heading     = routeHelper.getHeading
    val description = routeHelper.getDescription
    val operationId = routeHelper.getOperationId
    val ref         = routeHelper.getRef

    val paramsString = params.map(_.toYamlString).mkString("")
    val parameters   =
      if (paramsString.isEmpty) ""
      else s"""parameters:
      $paramsString
      """.trim

    val schema = "$ref: " + s"'../schema/store.yaml#/$ref'"

    if (httpVerb == "post") {
      s"""
$heading
  $verb:
    tags:
      - ${routeHelper.getCapitalizedTag}
    summary: $summary
    description: $description
    operationId: $operationId
    $parameters
    requestBody:
      $ref: '#/components/requestBodies/ExternalIdFormBody'
    responses:
      ${getResponse}
        description: ${routeHelper.getResponseDescription}
        content:
          application/json:
            schema:
              $schema
      """
    } else {
      s"""
$heading
  $verb:
    tags:
      - ${routeHelper.getCapitalizedTag}
    summary: $summary
    description: $description
    operationId: $operationId
    $parameters
    responses:
      ${getResponse}
        description: ${routeHelper.getResponseDescription}
        content:
          application/json:
            schema:
              $schema
      """
    }
  }

  def toYamlSummary: String = {
    val routeHelper = Route.getRouteHelper(httpVerb, path, method)
    val pathSummary = getPathSummary(path)
    val schema = "$ref: " + s""""./paths/${routeHelper.tag}.yaml#/${routeHelper.getHeading}""""

    s"""
$pathSummary:
  $schema
    """
  }

  def getPathSummary(path: String) = {
    val paths       = path.split("/").toSeq
    val pathSummary = paths
      .map(p => {
        if (p.startsWith(":")) s"{${p.replace(":", "")}}"
        else p
      })
      .mkString("/")

    if (pathSummary.startsWith("/")) s"'$pathSummary'" else s"'/$pathSummary'"
  }
}

object Route {

  def create(httpVerb: String, path: String, method: String, parameters: Seq[String]): Route = {
    val routeHelper = getRouteHelper(httpVerb, path, method)

    val params = parameters.map { param =>
      val Seq(paramName, paramType) = param.replace(",", "").trim().split(":").toSeq

      val name     = paramName.trim()
      val pType    = paramType.trim().toLowerCase()
      val location = if (path.contains(name)) ParamLocation.PathParam else ParamLocation.QueryParam
      Parameter(name, pType, "", location)
    }

    val actualParams = routeHelper.withAuthParams(params)
    Route(httpVerb, path, method, actualParams)
  }

  def getRouteHelper(httpVerb: String, path: String, method: String): RouteHelper = {
    val verb = httpVerb.toLowerCase

    val paths          = path.split("/").filter(_.nonEmpty)
    val tag            = paths(1)
    val capitalizedTag = tag.capitalize

    // method is of the form com.intercax.syndeia.controllers.external.BitBucketController.getContainers
    val names = method.split("\\.")

    // so methodName becomes getContainers
    val methodName = names.last

    // and qualifiedMethodParts becomes ["get", "Containers"], so the second item is going to be the name of the domain object
    val qualifiedMethodParts = methodName.split("(?=\\p{Upper})")

    // in case of types, the second and third item combined is the name of the type
    val tail       = qualifiedMethodParts.tail
    val domain     = if (methodName.contains("Type")) s"${tail.head}${tail.tail.head}" else tail.head.capitalize
    val domainName = getDomainName(domain)

    RouteHelper(tag, methodName, qualifiedMethodParts, domainName)
  }

  def getDomainName(domain: String): String = {
    if (domain.endsWith("ies")) domain.dropRight(3) + "y"
    else if (domain.endsWith("s")) domain.dropRight(1)
    else domain
  }

  def hasOnlyWhiteSpace(str: String): Boolean = {
    val trimmed = str.trim
    trimmed.forall(_.isWhitespace) || trimmed.forall(_ == ' ')
  }
}
