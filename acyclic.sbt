//-------------------------------------------------------
// Setup sbt acyclic
// Invocation: sbt clean compile
// See: https://github.com/lihaoyi/acyclic
//-------------------------------------------------------

addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.7")

libraryDependencies += "com.lihaoyi" %% "acyclic" % "0.1.7" % "provided"

autoCompilerPlugins := true