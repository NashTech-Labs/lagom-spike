import de.johoop.cpd4sbt.Language
import de.johoop.cpd4sbt.ReportType.XML
//------------------------------------
// Setup cpd for sbt
// Invocation: sbt cpd
// See: https://github.com/sbt/cpd4sbt
//------------------------------------

enablePlugins(CopyPasteDetector)

cpdReportType := XML

cpdReportName := "cpd.xml"

cpdTargetPath := target(_ / "cpd").value

cpdLanguage := Language.Scala