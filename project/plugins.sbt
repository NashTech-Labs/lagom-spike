logLevel := Level.Error

// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.3.0-RC2")

//code quality plugins
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
// end code quality plugins