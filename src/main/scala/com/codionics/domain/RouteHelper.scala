package com.codionics.domain

case class RouteHelper(tag: String, methodName: String, qualifiedMethodParts: Seq[String], domainName: String) {

  def getCapitalizedTag: String = tag.capitalize

  def qualifiedMethodPartsLower: Seq[String] = qualifiedMethodParts.map(_.toLowerCase)

  def getSummary: String = {
    val head = qualifiedMethodParts.head.capitalize
    val tail = qualifiedMethodParts.tail.mkString(" ")
    val article = getArticle(tag)

    if (methodName.endsWith("s")) s"$head ${getCapitalizedTag} $tail"
    else s"$head $article ${getCapitalizedTag} $tail"
  }

  def getHeading: String = {
    s"${qualifiedMethodPartsLower.head}-$tag-${qualifiedMethodPartsLower.tail.mkString("-")}"
  }

  def getDescription: String = {
    s"${qualifiedMethodPartsLower.head} $tag ${qualifiedMethodPartsLower.tail.mkString(" ")}"
  }

  def getResponseDescription: String = {
    val desc = getDescription
    val descArray = desc.split(" ").tail.map(_.capitalize)
    descArray.mkString(" ")
  }

  def getOperationId: String = {
    qualifiedMethodParts.patch(1, Seq(getCapitalizedTag), 0).mkString("")
  }

  def getRef: String = {
    if (methodName.endsWith("s")) s"${domainName}SeqMessage" else s"${domainName}ObjMessage"
  }

  def getAuthParams: Seq[Parameter] = {
    val userIdParam = Parameter.getUserIdParam(getCapitalizedTag)
    val extAuthTokenParam = Parameter.getExtAuthTokenParam(getCapitalizedTag)
    Seq(userIdParam, extAuthTokenParam)
  }

  def withAuthParams(params: Seq[Parameter]): Seq[Parameter] = {
    if (methodName.contains("Repositories")) params
    else params ++ getAuthParams    
  }

  def beginsWithVowel(s: String): Boolean = {
    val vowels = Seq("a", "e", "i", "o", "u")
    vowels.contains(s.head.toLower.toString)
  }

  def getArticle(s: String): String = {
    if (beginsWithVowel(s)) "an" else "a"
  }
}
