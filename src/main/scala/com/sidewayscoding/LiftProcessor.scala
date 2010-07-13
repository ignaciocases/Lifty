package com.sidewayscoding

import sbt._
import sbt.processor.BasicProcessor

import template.engine._
import template.util._
import java.io._
import java.util.Properties

// PROCESSOR 
class LiftProcessor extends SBTTemplateProcessor {
	def templates = SnippetTemplate :: MapperTemplate :: CometTemplate :: LiftProjectTemplate :: Nil 
	
}
object LiftProcessor extends LiftProcessor {}

// TEMPLATES 

trait DefaultLiftTemplate extends Template with Create with Delete{}

object SnippetTemplate extends DefaultLiftTemplate {
	
	def name = "snippet"
	def arguments = {
		object packageArgument extends Argument("pack") {
			override def transformationForPathValue(before: String) = Helper.pathOfPackage(before)
		}
		Argument("name") :: packageArgument ::  Nil
	}
	def files = {
	  val templatePath = "%s/snippet.ssp".format(GlobalConfiguration.rootResources)
	  val snippetPath = "src/main/scala/${pack}/${name}.scala"
	  TemplateFile(templatePath,snippetPath) :: Nil
	}
}
	
object MapperTemplate extends DefaultLiftTemplate {
	def name = "mapper"
		
	def arguments = {
		object packageArgument extends Argument("pack") {
			override def transformationForPathValue(before: String) = Helper.pathOfPackage(before)
		}
		object nameArgument extends Argument("name") with Default with Value{ value = "defaultValue" }
		object fieldArgument extends Argument("fields") with Repeatable with Optional
  	packageArgument :: nameArgument :: fieldArgument :: Nil
	}
	
  def files = {
    val templatePath = "%s/mapper.ssp".format(GlobalConfiguration.rootResources)
    val mapperPath = "src/main/scala/${pack}/${name}.scala"
    TemplateFile(templatePath,mapperPath) :: Nil
  }
}

object CometTemplate extends DefaultLiftTemplate {
	
	def name = "comet"
	
	def arguments = {
		object packageArgument extends Argument("pack") {
			override def transformationForPathValue(before: String) = Helper.pathOfPackage(before)
		}
		Argument("name") :: packageArgument ::  Nil
	}
	
	def files = {
	  val templatePath = "%s/comet.ssp".format(GlobalConfiguration.rootResources)
	  val snippetPath = "src/main/scala/${pack}/${name}.scala"
	  TemplateFile(templatePath,snippetPath) :: Nil
	}
	
}

object LiftProjectTemplate extends DefaultLiftTemplate {
	
	val basePath = "%s/basic-lift-project".format(GlobalConfiguration.rootResources)
	
	def name = "project"
	
	def arguments = {
		
		// the default main package should be the project organization
		val defaultMainPackage = { 
			val properties = new Properties()
			properties.load(new FileInputStream("project/build.properties"))
			properties.getProperty("project.organization") match {
				case null => "code" // default
				case str => str
			}
		}
		
		object mainPackage extends Argument("pack") with Default with Value { value = defaultMainPackage 
			override def transformationForPathValue(before: String) = Helper.pathOfPackage(before)
		}
		
		List(mainPackage)
	}
	
	override def postRenderAction(arguments: List[ArgumentResult]): Unit = {

		Helper.createFolderStructure(arguments)(
			"src/main/resources",
			"src/main/resources/props",
			"src/main/scala",
			"src/main/scala/bootstrap",
			"src/main/scala/bootstrap/liftweb",
			"src/main/scala/${pack}",
			"src/main/scala/${pack}/comet",
			"src/main/scala/${pack}/lib",
			"src/main/scala/${pack}/model",
			"src/main/scala/${pack}/snippet",
			"src/main/scala/${pack}/view",
			"src/main/webapp",
			"src/main/webapp/images",
			"src/main/webapp/templates-hidden",
			"src/main/webapp/static",
			"src/main/webapp/WEB-INF"
		)
		
		// test
		Helper.copy(
			"%s/test/LiftConsole.scala".format(basePath),
			"src/test/scala/LiftConsole.scala"
		)
		Helper.copy(
			"%s/test/RunWebApp.scala".format(basePath),
			"src/test/scala/RunWebApp.scala"
		)
		
		// resources
		Helper.copy(
     "%s/resources/props/default.props".format(basePath),
     "src/main/resources/props/default.props"
    )
		
		// webapp
    // Helper.copy(
    //      "%s/webapp/images/ajax-loader.gif".format(basePath),
    //      "src/main/webapp/images/ajax-loader.gif"
    //     )
    Helper.copy(
     "%s/webapp/static/index.html".format(basePath),
     "src/main/webapp/static/index.html"
    )
    Helper.copy(
     "%s/webapp/templates-hidden/default.html".format(basePath),
     "src/main/webapp/templates-hidden/default.html"
    )
    Helper.copy(
     "%s/webapp/templates-hidden/wizard-all.html".format(basePath),
     "src/main/webapp/templates-hidden/wizard-all.html"
    )
    Helper.copy(
     "%s/webapp/WEB-INF/web.xml".format(basePath),
     "src/main/webapp/WEB-INF/web.xml"
    )
    Helper.copy(
     "%s/webapp/index.html".format(basePath),
     "src/main/webapp/index.html"
    )
	}
		
	def files = {
		List( 
			TemplateFile(
				"%s/boot.ssp".format(basePath),
				"src/main/scala/bootstrap/liftweb/Boot.scala"
			),
			TemplateFile(
				"%s/helloworld.ssp".format(basePath),
				"src/main/scala/${pack}/snippet/HelloWorld.scala"
			),
			TemplateFile(
				"%s/user.ssp".format(basePath),
				"src/main/scala/${pack}/model/User.scala"
			),
			TemplateFile(
				"%s/dependencyFactory.ssp".format(basePath), 
				"src/main/scala/${pack}/lib/DependencyFactory.scala"
			),
			TemplateFile(
				"%s/test/HelloWorldTest.ssp".format(basePath),
				"src/test/scala/${pack}/snippet/HelloWorldTest.scala"
			),
			TemplateFile(
				"%s/test/AppTest.ssp".format(basePath),
				"src/test/scala/${pack}/AppTest.scala"
			),
			TemplateFile(
				"%s/ProjectDefinition.ssp".format(basePath),
				"project/build/ProjectDefinition.scala"
			)	
		)
	}
}