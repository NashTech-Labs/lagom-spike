//------------------------------------
// Setup ConductR with Project
// See: http://developers.lightbend.com/docs/reactive-platform/2.0/setup/setup-sbt.html
//------------------------------------

credentials += Credentials(Path.userHome / ".lightbend" / "commercial.credentials")
resolvers += "com-mvn" at "https://repo.lightbend.com/commercial-releases/"
resolvers += Resolver.url("con-ivy", url("https://repo.lightbend.com/commercial-releases/"))(Resolver.ivyStylePatterns)
