package com.codionics.domain

case class ParamLocation(in: String, required: Boolean) {

  override def toString = {
    s"""in: $in
        required: $required"""
  }
}

object ParamLocation {

  val PathParam  = ParamLocation("path", true)
  val QueryParam = ParamLocation("query", false)
  val HeaderParam = ParamLocation("header", true)
}
