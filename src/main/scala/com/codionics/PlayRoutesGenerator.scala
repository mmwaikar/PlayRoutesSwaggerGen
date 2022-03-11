package com.codionics

import com.codionics.parser.PlayRoutesParser
import com.codionics.domain.Route
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets
import scala.io.Source._

object PlayRoutesGenerator extends App {

  if (args.isEmpty) {
    println("Usage: PlayRoutesGenerator <play routes file>")
    sys.exit(1)
  }

  val lineSep    = System.lineSeparator()
  val routesFile = args(0)
  val playRoutes = fromFile(routesFile).getLines.toList
  // println(s"play route: ${playRoutes(0)}")

  val routesDoc = playRoutes.map { r =>
    val playRoute = PlayRoutesParser.parseAsRoute(r.trim())
    // println(s"play route: $playRoute")

    val yamlDoc = playRoute.toYamlString
    yamlDoc.trim()
  }

  val routesNoNewlines = routesDoc.filterNot(rd => Route.hasOnlyWhiteSpace(rd))
  Files.write(Paths.get("routes.yaml"), routesNoNewlines.mkString(lineSep).getBytes(StandardCharsets.UTF_8))
}
