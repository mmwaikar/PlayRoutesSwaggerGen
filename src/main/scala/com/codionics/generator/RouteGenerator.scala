package com.codionics.generator

import com.codionics.domain.Route

trait RouteGenerator {
  def generate(route: Route): String
}
