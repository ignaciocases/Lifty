package org.lifty.processor

import java.util.Properties
import java.io._
import net.liftweb.common._
import scala.io.Source

object LiftHelper {

  def liftFolderStructure: List[String] = List(
    "src/main/resources",
    "src/main/resources/props",
    "src/main/scala",
    "src/main/scala/bootstrap",
    "src/main/scala/bootstrap/liftweb",
    "src/main/scala/${mainpack}",
    "src/main/scala/${mainpack}/comet",
    "src/main/scala/${mainpack}/lib",
    "src/main/scala/${mainpack}/model",
    "src/main/scala/${mainpack}/snippet",
    "src/main/scala/${mainpack}/view",
    "src/main/webapp",
    "src/main/webapp/images",
    "src/main/webapp/templates-hidden",
    "src/main/webapp/static",
    "src/main/webapp/WEB-INF")

  def searchForMainPackage: Box[String] = {
    val properties = new Properties()
    properties.load(new FileInputStream("project/build.properties"))
    properties.getProperty("project.organization") match {
      case null => Empty
      case str => Full(str)
    }
  }

  /**
   * Searches for the main package used for the app in the Boot.scala class. If 
   * the boot file doesn't exist it will search the properties file
   * 
   * @return A box with the string, if successful. 
   */
  def searchForPackageInBoot(pathToBoot: String, append: Box[String]): Box[String] = {
    val regex = """\("(\S*)"\)""".r
    val file = new File(pathToBoot)
    if (file.exists) {
      val is = new FileInputStream(file)
      val in = scala.io.Source.fromInputStream(is)
      in.getLines.filter(_.contains("LiftRules.addToPackages")).toList match {
        case head :: rest => regex.findFirstMatchIn(head) match {
          case Some(m) => Full(m.group(1) + append.openOr(""))
          case None => Failure("Regxp search in boot failed")
        }
        case Nil => Failure("No lines contained LiftRules.addToPackages")
      }
    } else {
      searchForMainPackage match {
        case Full(str) => Full(str + append.openOr(""))
        case _ => Empty
      }
    }
  }

}
