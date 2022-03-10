package com.codionics.parser

import com.codionics.BaseSpec

class PlayRoutesParserSpec extends BaseSpec {
  val route =
    "GET     /external/bitbucket/:repoKey/containers                     com.intercax.syndeia.controllers.external.BitBucketController.getContainers(repoKey: String, `type.externalKey`: Option[String], `other.workspace`: Option[String], page: Option[String], perPage: Option[Int])"

  "The PlayRoutesParser" should "parse a Play route as a string" in {
    val parsedRoute = PlayRoutesParser.parseAsString(route)
    parsedRoute should not be null

    val parsedVal = parsedRoute.get.value
    parsedVal should not(be(null) or be(empty))
    println(s"parsedVal: $parsedVal")
  }

  it should "parse a Play route as a tuple" in {
    val parsedRoute = PlayRoutesParser.parseAsTuple(route)
    parsedRoute should not be null

    val parsedVal = parsedRoute.get.value
    parsedVal should not be null
    println(s"parsedVal: $parsedVal")

    val (httpVerb, path, method, (Seq(first, tail @ _*), _)) = parsedVal
    println(s"httpVerb: ${httpVerb}")
    println(s"path: ${path}")
    println(s"method: ${method}")
    println(s"first: ${first}")
  }

  it should "parse a Play route as a Route" in {
    val playRoute = PlayRoutesParser.parseAsRoute(route)
    playRoute should not be null
    println(s"play route: $playRoute")
  }
}
