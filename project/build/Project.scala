import sbt._

class Project(info: ProjectInfo) extends ProcessorProject(info) with ProguardProject {
	
	val scalatools_release = "Scala Tools Releases" at "http://scala-tools.org/repo-releases/"
	val scalatools_snapshots = "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"
	
  val sbt_template_engine = "org.lifty" %% "lifty-engine" % "0.4-SNAPSHOT"
  val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
  	
	override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
	Credentials(Path.userHome / "dev" / ".nexus_credentials", log)
	
	//proguard plugin customizations
	override def proguardOptions = List(
	    proguardKeepMain("org.lifty.processor.LiftProcessor")
    )
	override def proguardInJars = super.proguardInJars +++ scalaLibraryPath
	
}