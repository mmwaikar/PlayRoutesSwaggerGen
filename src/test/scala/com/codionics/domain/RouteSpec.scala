package com.codionics.domain

import com.codionics.BaseSpec
import com.codionics.domain._

class RouteSpec extends BaseSpec {

  val path           = "/external/collaborator/:repoKey/types/container"
  val method         = "com.intercax.syndeia.controllers.external.CollaboratorController.getContainers"
  val methodForTypes = "com.intercax.syndeia.controllers.external.CollaboratorController.getContainerTypeByExternalKey"

  "The Route.getRouteHelper method" should "generate correct RouteHelper for types" in {
    val routeHelper = Route.getRouteHelper("GET", path, methodForTypes)
    routeHelper should not be null

    routeHelper.domainName should be("ContainerType")
    routeHelper.getRef should be("ContainerTypeObjMessage")
  }

  it should "generate correct RouteHelper for main domain objects" in {
    val routeHelper = Route.getRouteHelper("GET", path, method)
    routeHelper should not be null

    routeHelper.domainName should be("Container")
    routeHelper.getRef should be("ContainerSeqMessage")
  }
}
