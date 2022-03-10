package com.codionics.parser

import com.codionics.BaseSpec
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets

class PlayRoutesParserSpec extends BaseSpec {
  val route =
    "GET     /external/bitbucket/:repoKey/containers                     com.intercax.syndeia.controllers.external.BitBucketController.getContainers(repoKey: String, `type.externalKey`: Option[String], `other.workspace`: Option[String], page: Option[String], perPage: Option[Int])"

  val routes = """
  GET      /external/artifactory/repositories                                    com.intercax.syndeia.controllers.external.ArtifactoryController.getRepositories()
  GET      /external/artifactory/repositories/:key                               com.intercax.syndeia.controllers.external.ArtifactoryController.getRepositoryByExternalKey(key: String)
  POST     /external/artifactory/repositories/external/id                        com.intercax.syndeia.controllers.external.ArtifactoryController.getRepositoryByExternalId()
  GET      /external/artifactory/:repoKey/containers                             com.intercax.syndeia.controllers.external.ArtifactoryController.getContainers(repoKey: String,`type.externalKey`:Option[String])
  GET      /external/artifactory/:repoKey/containers/:key                        com.intercax.syndeia.controllers.external.ArtifactoryController.getContainerByExternalKey(repoKey: String, key: String,`type.externalKey`:Option[String])
  POST     /external/artifactory/:repoKey/containers/external/id                 com.intercax.syndeia.controllers.external.ArtifactoryController.getContainerByExternalId(repoKey: String)
  POST     /external/artifactory/:repoKey/containers/search                      com.intercax.syndeia.controllers.external.ArtifactoryController.containerSearch(repoKey: String)
  GET      /external/artifactory/:repoKey/artifacts                              com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifacts(repoKey: String, `container.externalKey`: Option[String], `type.externalKey`: Option[String], `other.parent`: Option[String])
  GET      /external/artifactory/:repoKey/artifacts/:key                         com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifactByExternalKey(repoKey: String, key: String, `container.externalKey`: Option[String], `type.externalKey`: Option[String])
  POST     /external/artifactory/:repoKey/artifacts/external/id                  com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifactByExternalId(repoKey: String)
  POST     /external/artifactory/:repoKey/artifacts/search                       com.intercax.syndeia.controllers.external.ArtifactoryController.artifactSearch(repoKey: String)
  GET      /external/artifactory/:repoKey/relations                              com.intercax.syndeia.controllers.external.ArtifactoryController.getRelations(repoKey: String, `container.externalKey`: Option[String], `type.externalKey`: Option[String], `source.externalKey`: Option[String], `target.externalKey`: Option[String])
  GET      /external/artifactory/:repoKey/relations/:externalKey                 com.intercax.syndeia.controllers.external.ArtifactoryController.getRelationByExternalKey(repoKey: String, externalKey: String, `container.externalKey`: Option[String])
  POST     /external/artifactory/:repoKey/relations/external/id                  com.intercax.syndeia.controllers.external.ArtifactoryController.getRelationByExternalId(repoKey: String)
  GET      /external/artifactory/:repoKey/types/container                        com.intercax.syndeia.controllers.external.ArtifactoryController.getContainerTypes(repoKey: String)
  GET      /external/artifactory/:repoKey/types/container/:externalKey           com.intercax.syndeia.controllers.external.ArtifactoryController.getContainerTypeByExternalKey(repoKey: String, externalKey: String)
  POST     /external/artifactory/:repoKey/types/container/external/id            com.intercax.syndeia.controllers.external.ArtifactoryController.getContainerTypeByExternalId(repoKey: String)
  GET      /external/artifactory/:repoKey/types/artifact                         com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifactTypes(repoKey: String)
  GET      /external/artifactory/:repoKey/types/artifact/:externalKey            com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifactTypeByExternalKey(repoKey: String, externalKey: String)
  POST     /external/artifactory/:repoKey/types/artifact/external/id             com.intercax.syndeia.controllers.external.ArtifactoryController.getArtifactTypeByExternalId(repoKey: String)
  GET      /external/artifactory/:repoKey/types/relation                         com.intercax.syndeia.controllers.external.ArtifactoryController.getRelationTypes(repoKey: String)
  GET      /external/artifactory/:repoKey/types/relation/:externalKey            com.intercax.syndeia.controllers.external.ArtifactoryController.getRelationTypeByExternalKey(repoKey: String, externalKey: String)
  POST     /external/artifactory/:repoKey/types/relation/external/id             com.intercax.syndeia.controllers.external.ArtifactoryController.getRelationTypeByExternalId(repoKey: String)
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

    val routesDoc = playRoutes.map { r =>
      val playRoute = PlayRoutesParser.parseAsRoute(r.trim())
      playRoute should not be null
      // println(s"play route: $playRoute")

      val yamlDoc = playRoute.toYamlString
      yamlDoc should not be null
      yamlDoc
    }

    Files.write(Paths.get("routes.yaml"), routesDoc.mkString("\n").getBytes(StandardCharsets.UTF_8))
  }
}
