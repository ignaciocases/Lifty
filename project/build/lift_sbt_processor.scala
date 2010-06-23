import sbt._

class LiftSBTProcessor(info: ProjectInfo) extends ProcessorProject(info) {
	
	// repos
	val nexus = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
	// dependencies
	val scalate = "org.fusesource.scalate" % "scalate-core" % "1.0-local" from "http://github.com/downloads/mads379/Simple-Build-Tool-Template-Engine/scalate-core-1.0-SNAPSHOT.jar"
	val sbt_template_engine = "com.sidewayscoding" % "sbt_template_engine_2.7.7" % "0.1"
	
	// PUBLISHING
	override def managedStyle = ManagedStyle.Maven
	val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
	Credentials(Path.userHome / "dev" / ".nexus_credentials", log)
	
}