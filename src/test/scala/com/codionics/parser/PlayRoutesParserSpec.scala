package com.codionics.parser

import com.codionics.BaseSpec

class PlayRoutesParserSpec extends BaseSpec {
  val route =
    "GET     /external/bitbucket/:repoKey/containers                     com.intercax.syndeia.controllers.external.BitBucketController.getContainers(repoKey: String, `type.externalKey`: Option[String], `other.workspace`: Option[String], page: Option[String], perPage: Option[Int])"

  val routes = """
  GET      /external/doors/repositories                                    com.intercax.syndeia.controllers.external.DOORSController.getRepositories()
  GET      /external/doors/repositories/:key                               com.intercax.syndeia.controllers.external.DOORSController.getRepositoryByExternalKey(key: String)
  POST     /external/doors/repositories/external/id                        com.intercax.syndeia.controllers.external.DOORSController.getRepositoryByExternalId()
  GET      /external/doors/:repoKey/containers                             com.intercax.syndeia.controllers.external.DOORSController.getContainers(repoKey: String,`type.key`:Option[String])
  GET      /external/doors/:repoKey/containers/:key                        com.intercax.syndeia.controllers.external.DOORSController.getContainerByExternalKey(repoKey: String, key: String)
  POST     /external/doors/:repoKey/containers/external/id                 com.intercax.syndeia.controllers.external.DOORSController.getContainerByExternalId(repoKey: String)
  POST     /external/doors/:repoKey/containers/search                      com.intercax.syndeia.controllers.external.DOORSController.containerSearch(repoKey: String)
  GET      /external/doors/:repoKey/artifacts                              com.intercax.syndeia.controllers.external.DOORSController.getArtifacts(repoKey: String, `container.externalKey`: Option[String], `type.externalKey`: Option[String], `other.folder`: Option[String], `other.collection`: Option[String], `other.resource`: Option[String], `other.component`: Option[String], `other.configuration`: Option[String], page: Option[String], pageSize: Option[Int])
  GET      /external/doors/:repoKey/artifacts/:key                         com.intercax.syndeia.controllers.external.DOORSController.getArtifactByExternalKey(repoKey: String, key: String, `type.externalKey`: Option[String], `container.externalKey`: Option[String], `other.folder`: Option[String], `other.component`: Option[String], `other.configuration`: Option[String])
  POST     /external/doors/:repoKey/artifacts/external/id                  com.intercax.syndeia.controllers.external.DOORSController.getArtifactByExternalId(repoKey: String)
  GET      /external/doors/:repoKey/relations                              com.intercax.syndeia.controllers.external.DOORSController.getRelations(repoKey:String, `container.externalKey` : Option[String],  `type.externalKey` : Option[String],  externalKey : Option[String], `source.externalKey` : Option[String],  `target.externalKey` : Option[String], `other.configuration`: Option[String])
  GET      /external/doors/:repoKey/relation/:key                          com.intercax.syndeia.controllers.external.DOORSController.getRelationByExternalKey(repoKey: String, key: String, `other.configuration`: Option[String])
  POST     /external/doors/:repoKey/relation/external/id                   com.intercax.syndeia.controllers.external.DOORSController.getRelationByExternalId(repoKey: String)
  GET      /external/doors/:repoKey/types/artifact                         com.intercax.syndeia.controllers.external.DOORSController.getArtifactTypes(repoKey: String, `container.externalKey` : Option[String])
  GET      /external/doors/:repoKey/types/artifact/:externalKey            com.intercax.syndeia.controllers.external.DOORSController.getArtifactTypeByExternalKey(repoKey: String, externalKey: String, `container.externalKey` : Option[String])
  POST     /external/doors/:repoKey/types/artifact/external/id             com.intercax.syndeia.controllers.external.DOORSController.getArtifactTypeByExternalId(repoKey: String)
  GET      /external/doors/:repoKey/types/container                        com.intercax.syndeia.controllers.external.DOORSController.getContainerTypes(repoKey: String)
  GET      /external/doors/:repoKey/types/container/:externalKey           com.intercax.syndeia.controllers.external.DOORSController.getContainerTypeByExternalKey(repoKey: String, externalKey: String)
  POST     /external/doors/:repoKey/types/container/external/id            com.intercax.syndeia.controllers.external.DOORSController.getContainerTypeByExternalId(repoKey: String)
  POST     /external/doors/:repoKey/artifacts/search                       com.intercax.syndeia.controllers.external.DOORSController.artifactSearch(repoKey: String, `other.configuration`: Option[String])
  GET      /external/doors/:repoKey/signIn                                 com.intercax.syndeia.controllers.external.DOORSController.signIn(repoKey: String)
  GET      /external/doors/:repoKey/signOut                                com.intercax.syndeia.controllers.external.DOORSController.signOut(repoKey: String)
  """

  "The PlayRoutesParser" should "parse a Play route as a string" in {
    val parsedRoute = PlayRoutesParser.parseAsString(route)
    parsedRoute should not be null

    val parsedVal = parsedRoute.get.value
    parsedVal should not(be(null) or be(empty))
    // println(s"parsedVal: $parsedVal")
  }

  it should "parse a Play route as a tuple" in {
    val parsedRoute = PlayRoutesParser.parseAsTuple(route)
    parsedRoute should not be null

    val parsedVal = parsedRoute.get.value
    parsedVal should not be null
    // println(s"parsedVal: $parsedVal")

    val (httpVerb, path, method, (Seq(first, tail @ _*), _)) = parsedVal
    // println(s"httpVerb: ${httpVerb}")
    // println(s"path: ${path}")
    // println(s"method: ${method}")
    // println(s"first: ${first}")
  }

  it should "parse a Play route as a Route" in {
    val playRoute = PlayRoutesParser.parseAsRoute(route)
    playRoute should not be null
    // println(s"play route: $playRoute")
  }

  it should "generate the YAML documentation for a route" in {
    val playRoute = PlayRoutesParser.parseAsRoute(route)
    playRoute should not be null
    // println(s"play route: $playRoute")

    val yamlDoc = playRoute.toYamlString
    yamlDoc should not be null
    // println(s"yaml doc for the route: $yamlDoc")
  }

  it should "generate the YAML documentation for routes" in {
    val playRoutes = routes.split("\r\n").filter(_.trim.nonEmpty)
    playRoutes should not be null
    println(s"play route: ${playRoutes(0)}")

    playRoutes.foreach { r =>
      val playRoute = PlayRoutesParser.parseAsRoute(r.trim())
      playRoute should not be null
      // println(s"play route: $playRoute")

      val yamlDoc = playRoute.toYamlString
      yamlDoc should not be null
      println(s"$yamlDoc")
    }
  }
}
