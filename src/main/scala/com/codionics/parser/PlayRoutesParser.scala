package com.codionics.parser

import com.codionics.domain._
import fastparse._, NoWhitespace._

object PlayRoutesParser {

  /** Parses one or more space " " characters.
    */
  def spacesP[_: P] = P(" ".rep)

  /** Parses an HTTP verb i.e. Get, Post, Put etc. ignoring the case.
    */
  def httpVerbP[_: P] = P(StringInIgnoreCase("GET", "POST", "PUT", "DELETE").!.rep(1))

  /** Parses spaces followed by a colon ":".
    */
  def spacesThenColonP[_: P] = P(spacesP ~ ":")

  /** Parses spaces followed by a comma ",".
    */
  def spacesThenCommaP[_: P] = P(spacesP ~ ",")

  /** Parses a bracket open "(" followed by spaces.
    */
  def bracketOpenThenSpacesP[_: P] = P("(" ~ spacesP)

  /** Parses spaces followed by a bracket close ")".
    */
  def spacesThenBracketCloseP[_: P] = P(spacesP ~ ")")

  /** Parses the url path fragment for a (Play based) REST endpoint.
    */
  def pathP[_: P] = P(CharsWhileIn("a-zA-Z/:", 0).!)

  /** Parses a variable or a method parameter name.
    */
  def varNameP[_: P] = P(CharsWhileIn("a-zA-Z._`").!)

  /** Parses a variable or method parameter type (including Scala Option[] values).
    */
  def optionalVarTypeP[_: P] = P(("Option[" ~ varNameP ~ "]").!)
  def varTypeP[_: P]         = P((optionalVarTypeP | varNameP).!)

  /** Parses the name and type of a method parameter.
    */
  def varNameTypeP[_: P] = P((spacesP ~ varNameP ~ spacesThenColonP ~ spacesP ~ varTypeP).!)

  /** Parses a list of name and types of method parameters.
    */
  def varNameTypesP[_: P] = P((varNameTypeP ~ ("," | spacesThenCommaP | spacesP)).!.rep)

  /** Parses a single Play route including the HTTP verb.
    */
  def routeParser[_: P] = P((httpVerbP ~ spacesP ~ pathP ~ spacesP ~ varNameP ~ "(" ~ spacesP ~ varNameTypesP ~ ")").!)

  /** Parses a single Play route including the HTTP verb and returns the result as a tuple.
    */
  def routeParserT[_: P] = P( httpVerbP.! ~ spacesP ~ pathP.! ~ spacesP ~ varNameP.! ~ ("(" ~ spacesP ~ varNameTypesP ~ ")".!) )

  def parseAsString(route: String): Parsed[String] = {
    parse(route, routeParser(_))
  }

  def parseAsTuple(route: String): Parsed[(String, String, String, (Seq[String], String))] = {
    parse(route, routeParserT(_))
  }

  def parseAsRoute(route: String): Route = {
    // println(s"parsing route: $route")
    val parsedRoute = parseAsTuple(route)
    val parsedVal = parsedRoute.get.value
    
    val (httpVerb, path, method, tuple) = parsedVal
    Route.create(httpVerb, path, method, tuple._1)
  }
}
