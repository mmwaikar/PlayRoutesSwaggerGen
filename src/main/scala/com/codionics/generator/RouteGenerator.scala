package com.codionics.generator

import com.codionics.parser.Route

trait RouteGenerator {
  def generate(route: Route): String
}
