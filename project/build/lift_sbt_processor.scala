import sbt._

class LiftSBTProcessor(info: ProjectInfo) extends ProcessorProject(info) {
	
	val scalatools_release = "Scala Tools Releases" at "http://scala-tools.org/repo-releases/"
	val scalatools_snapshots = "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"
	
	val sbt_template_engine = "com.sidewayscoding" % "sbt_template_engine_2.7.7" % "0.1-SNAPSHOT"
	
	override def managedStyle = ManagedStyle.Maven
	//val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
	val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
	Credentials(Path.userHome / "dev" / ".nexus_credentials", log)
	
}