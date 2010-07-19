package com.sidewayscoding

import LiftHelper._
import sbt._
import sbt.processor.BasicProcessor

import template.engine._
import template.util.TemplateHelper._
import net.liftweb.common._

trait DefaultLiftTemplate extends Template with Create with Delete{}

object SnippetTemplate extends DefaultLiftTemplate {
	
	def name = "snippet"
	
	def description = "Creates a snippet"
	
	def arguments = {
		object packageArgument extends Argument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala") match {
		    case Full(packageName) => packageName + ".snippet"
		    case _ => "code.snippet" // At some point it should request a package here
		  }
			override def transformationForPathValue(before: String) = pathOfPackage(before)
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
	
	def description = "Creates a model class using Mapper"
	
	override def notice(argResults: List[ArgumentResult]) = 
	  Full(replaceVariablesInPath("Remember to add ${name} to the Schemifier in your boot.scala file", argResults))
		
	def arguments = {
		object packageArgument extends Argument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala") match {
		    case Full(packageName) => packageName + ".model"
		    case _ => "code.model" // At some point it should request a package here
		  }
			override def transformationForPathValue(before: String) = pathOfPackage(before)
		}
		object nameArgument extends Argument("name") with Default with Value{ value = "defaultValue" }
		object fieldArgument extends Argument("fields") with Repeatable with Optional
  	
  	nameArgument :: packageArgument :: fieldArgument :: Nil
	}
	
  def files = {
    val templatePath = "%s/mapper.ssp".format(GlobalConfiguration.rootResources)
    val mapperPath = "src/main/scala/${pack}/${name}.scala"
    TemplateFile(templatePath,mapperPath) :: Nil
  }
}

object CometTemplate extends DefaultLiftTemplate {
	
	def name = "comet"
	
	def description = "Creates a comet component"
	
	override def notice(argResults: List[ArgumentResult]) = 
	  Full(replaceVariablesInPath("Add <lift:comet type='${name}' /> in a template file to use the comet component", argResults))
	
	def arguments = {
		object packageArgument extends Argument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala") match {
		    case Full(packageName) => packageName + ".comet"
		    case _ => "code.model" // At some point it should request a package here
		  }
			override def transformationForPathValue(before: String) = pathOfPackage(before)
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
	
	lazy val defaultMainPackage = searchForMainPackage match {
	  case Full(pack) => pack
	  case _ => "code"
	}
	
	def name = "project"
	
	def description = "Creates a Lift project with some functionality to get you started"
	
	def arguments = {
			
		object mainPackage extends Argument("pack") with Default with Value { 
		  value = defaultMainPackage 
			override def transformationForPathValue(before: String) = pathOfPackage(before)
		}
		
		mainPackage :: Nil
	}
	
	override def postRenderAction(arguments: List[ArgumentResult]): Unit = {

		createFolderStructure(arguments)(
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
		
		copy("%s/test/LiftConsole.scala".format(basePath),"src/test/scala/LiftConsole.scala")
		copy("%s/test/RunWebApp.scala".format(basePath),"src/test/scala/RunWebApp.scala")
		copy("%s/resources/props/default.props".format(basePath),"src/main/resources/props/default.props")
    copy("%s/webapp/static/index.html".format(basePath),"src/main/webapp/static/index.html")
    copy("%s/webapp/templates-hidden/default.html".format(basePath),"src/main/webapp/templates-hidden/default.html")
    copy("%s/webapp/templates-hidden/wizard-all.html".format(basePath),"src/main/webapp/templates-hidden/wizard-all.html")
    copy("%s/webapp/WEB-INF/web.xml".format(basePath),"src/main/webapp/WEB-INF/web.xml")
    copy("%s/webapp/index.html".format(basePath),"src/main/webapp/index.html")
	}
		
	def files = {
		TemplateFile("%s/boot.ssp".format(basePath),"src/main/scala/bootstrap/liftweb/Boot.scala") :: 
		TemplateFile("%s/helloworld.ssp".format(basePath),"src/main/scala/${pack}/snippet/HelloWorld.scala") :: 
		TemplateFile("%s/user.ssp".format(basePath),"src/main/scala/${pack}/model/User.scala") :: 
		TemplateFile("%s/dependencyFactory.ssp".format(basePath), "src/main/scala/${pack}/lib/DependencyFactory.scala") :: 
		TemplateFile("%s/test/HelloWorldTest.ssp".format(basePath),"src/test/scala/${pack}/snippet/HelloWorldTest.scala") :: 
		TemplateFile("%s/test/AppTest.ssp".format(basePath),"src/test/scala/${pack}/AppTest.scala") :: 
		TemplateFile("%s/ProjectDefinition.ssp".format(basePath),"project/build/ProjectDefinition.scala")	:: 
		Nil
	}
}

class LiftProcessor extends SBTTemplateProcessor {
	def templates = SnippetTemplate :: MapperTemplate :: CometTemplate :: LiftProjectTemplate :: Nil 	
}

object LiftProcessor extends LiftProcessor {}
