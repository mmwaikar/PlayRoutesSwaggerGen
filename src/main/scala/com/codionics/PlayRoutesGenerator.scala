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
  val playRoutes = fromFile(routesFile).getLines.toList.filterNot(l => l.startsWith("#") || l.trim.isEmpty)
  // println(s"play route: ${playRoutes(0)}")

  val routesDoc = playRoutes.map { r =>
    val playRoute = PlayRoutesParser.parseAsRoute(r.trim())
    // println(s"play route: $playRoute")

    val yamlDoc = playRoute.toYamlString
    val yamlSummary = playRoute.toYamlSummary
    (yamlDoc.trim(), yamlSummary.trim())
  }

  val routesNoNewlines = routesDoc.map(_._1).filterNot(rd => Route.hasOnlyWhiteSpace(rd))
  writeToFile("routes.yaml", routesNoNewlines)

  val summaryNoNewlines = routesDoc.map(_._2).filterNot(rd => Route.hasOnlyWhiteSpace(rd))
  writeToFile("summary.yaml", summaryNoNewlines)

  private def writeToFile(fileName: String, contents: List[String]) = {
    Files.write(Paths.get(fileName), contents.mkString(lineSep).getBytes(StandardCharsets.UTF_8))
  }
}
