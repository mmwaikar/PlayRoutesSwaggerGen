package com.codionics.domain

case class Parameter(
    name: String,
    `type`: String,
    description: String,
    location: ParamLocation,
    style: String = "explode",
    explode: Boolean = true
) {

  def toYamlString: String = {
    val withoutOption = `type`.replace("option", "").replace("[", "").replace("]", "")

    s"""
      - name: $name
        description: $description
        ${location.toString}
        style: $style
        explode: $explode
        schema:
          type: ${withoutOption}
    """
  }
}

object Parameter {

  def getUserIdParam(tag: String) = Parameter("User-Id", "string", s"$tag UserName", ParamLocation.HeaderParam, "simple")

  def getExtAuthTokenParam(tag: String) = Parameter("Ext-Auth-Token", "string", s"$tag Password", ParamLocation.HeaderParam, "simple")
}
