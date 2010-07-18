package com.sidewayscoding

import java.util.Properties
import net.liftweb.common._
import java.io._
import scala.io.Source

object LiftHelper {
  
  def searchForMainPackage: Box[String] = {
    val properties = new Properties()
		properties.load(new FileInputStream("project/build.properties"))
		properties.getProperty("project.organization") match {
			case null => Empty
			case str => Full(str)
		}
  }
  
  
  /**
  * Searches for the main package used for the app in the Boot.scala class
  * 
  * @return A box with the string, if successfull. 
  */
  def searchForPackageInBoot(pathToBoot: String): Box[String] = {
    val regex = """\("(\S*)"\)""".r
    val file = new File(pathToBoot)
    if (file.exists) {
      val is = new FileInputStream(file)
      val in = scala.io.Source.fromInputStream(is)
      in.getLines.filter( _.contains("LiftRules.addToPackages")).toList match {
        case head :: rest => regex.findFirstMatchIn(head) match {
          case Some(m) => Full(m.group(1))
          case None => Failure("Regxp search in boot failed")
        }
        case Nil => Failure("No lines contained LiftRules.addToPackages")
      }
    } else {
      Failure("The file %s does not exist".format(pathToBoot))
    }
  }
  
}
