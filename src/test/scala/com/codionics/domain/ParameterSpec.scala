package com.codionics.domain

import com.codionics.BaseSpec
import com.codionics.domain._

class ParameterSpec extends BaseSpec {

  "The Parameter" should "generate proper string" in {
    val parameter =
      Parameter("repoKey", "string", "Syndeia key of the owning repository", ParamLocation.PathParam, "simple")
    // println(s"parameter string: ${parameter.toYamlString}")
  }
}
