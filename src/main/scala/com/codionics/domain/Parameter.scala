package com.codionics.domain

case class Parameter(
    name: String,
    `type`: String,
    description: String,
    location: ParamLocation,
    style: String = "explode",
    explode: Boolean = true
) {

  override def toString = {
    s"""
      - name: $name
        description: $description
        ${location.toString}
        style: $style
        explode: $explode
        schema:
          type: ${`type`}
    """
  }
}
