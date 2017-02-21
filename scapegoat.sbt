//------------------------------------
// Setup scapegoat for sbt
// Invocation: sbt scapegoat
// See: https://github.com/sksamuel/sbt-scapegoat
//------------------------------------
import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._

scapegoatVersion := "1.1.0"

scapegoatIgnoredFiles := Seq(".*/Launcher.scala")

scapegoatConsoleOutput := false