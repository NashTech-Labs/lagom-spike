//------------------------------------
// Setup scoverage for sbt
// Invocation: sbt clean coverage test && sbt coverageReport && sbt coverageAggregate
// See: https://github.com/scoverage/sbt-scoverage
//------------------------------------

coverageMinimum := 90

coverageFailOnMinimum := true