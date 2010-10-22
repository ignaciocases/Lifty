import sbt._

class Project(info: ProjectInfo) extends ProcessorProject(info) with AssemblyProject {

  val scalatools_release = "Scala Tools Releases" at "http://scala-tools.org/repo-releases/"
  val scalatools_snapshots = "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"

  val sbt_template_engine = "org.lifty" %% "lifty-engine" % "0.4-SNAPSHOT"
  val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"

  override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
  Credentials(Path.userHome / "dev" / ".nexus_credentials", log)

  override def mainClass: Option[String] = Some("org.lifty.processor.LiftyStandAlone")
}

/**
 * This has pretty much been copied from N8hans repository:
 * http://github.com/n8han/ants/blob/master/project/build/AntsProject.scala
 */
trait AssemblyProject { 
  
  this: ProcessorProject =>

  def assemblyExclude(base: PathFinder) = base / "META-INF" ** "*"
  def assemblyOutputPath = outputPath / assemblyJarName
  def assemblyJarName = artifactID + "-standalone-" + version + ".jar"
  def assemblyTemporaryPath = outputPath / "assemblage"
  def assemblyClasspath = runClasspath
  def assemblyExtraJars = mainDependencies.scalaJars

  lazy val expandLibs = expandLibsAction

  def expandLibsAction = expandLibsTask(assemblyTemporaryPath, assemblyClasspath, assemblyExtraJars) dependsOn (compile)

  def expandLibsTask(tempDir: Path, classpath: PathFinder, extraJars: PathFinder): Task = task {
    val libs = classpath.get.filter(ClasspathUtilities.isArchive)
    for (jar <- (libs ++ extraJars.get)) {
      log.info(jar.toString)
      FileUtilities.unzip(jar, tempDir, log)
    }
    None
  }

  def assemblyFiles = descendents(assemblyTemporaryPath ##, "*") --- assemblyExclude(assemblyTemporaryPath ##)

  def assemblyPackagePaths = packagePaths +++ assemblyFiles

  lazy val assembly = assemblyAction

  def assemblyAction = assemblyTask(assemblyPackagePaths) dependsOn (compile, expandLibs)

  def assemblyTask(packagePaths: PathFinder) =
    packageTask(packagePaths, assemblyOutputPath, packageOptions)
}
