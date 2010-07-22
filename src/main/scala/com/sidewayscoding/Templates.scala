package com.sidewayscoding

import LiftHelper._
import sbt._
import sbt.processor.BasicProcessor

import template.engine._
import template.util.TemplateHelper._
import net.liftweb.common._

trait DefaultLiftTemplate extends Template with Create with Delete

object SnippetTemplate extends DefaultLiftTemplate {
	
	def name = "snippet"
	
	def description = "Creates a snippet"
	
	def arguments = {
		object packageArgument extends PackageArgument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala", Full(".snippet"))
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
		object packageArgument extends PackageArgument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".model"))
		}
		object nameArgument extends Argument("name")
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
		object packageArgument extends PackageArgument("pack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".comet"))
		}
		Argument("name") :: packageArgument ::  Nil
	}
	
	def files = {
	  val templatePath = "%s/comet.ssp".format(GlobalConfiguration.rootResources)
	  val snippetPath = "src/main/scala/${pack}/${name}.scala"
	  TemplateFile(templatePath,snippetPath) :: Nil
	}
	
}

/**
* Tempalte for a model class implemented using Mapper. It provides basic functionality
* that would expect from a User.
*/
object UserTemplate extends DefaultLiftTemplate {
  
  def name = "user"
  
  def description = "Tempalte for a model class implemented using Mapper. It provides basic functionality that would expect from a User"
  
  def arguments = pack :: Nil
  
  def files = TemplateFile(
    "%s/basic-lift-project/user.ssp".format(GlobalConfiguration.rootResources),
    "src/main/scala/${pack}/User.scala"
  ) :: Nil
  
  object pack extends PackageArgument("pack") with Default with Value { 
    value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".model"))
  }
}

/**
* I really don't know what this tempalte is for.
*/
object DependencyFactory extends DefaultLiftTemplate {
  
  def name = "dependencyFactory"
  
  def description = "I really don't know what this is used for, Tim? David?"
  
  def arguments = pack :: Nil
  
  def files = TemplateFile(
    "%s/basic-lift-project/dependencyFactory.ssp".format(GlobalConfiguration.rootResources),
    "src/main/scala/${pack}/DependencyFactory.scala"
  ) :: Nil
  
  object pack extends PackageArgument("pack") with Default with Value { 
    value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".model"))
  }
  
}

object BlankLiftProject extends DefaultLiftTemplate {
  
  def name = "project-blank"
  
  def description = "Creates a blank Lift project that uses SBT as it's build system"
  
  def arguments = pack :: Nil
  
  def files = {
    TemplateFile("%s/blank-lift-project/ProjectDefinition.scala".format(GlobalConfiguration.rootResources),"project/build/Project.scala") ::
    TemplateFile("%s/basic-lift-project/test/LiftConsole.scala".format(GlobalConfiguration.rootResources),"src/test/scala/LiftConsole.scala") :: 
		TemplateFile("%s/basic-lift-project/test/RunWebApp.scala".format(GlobalConfiguration.rootResources),"src/test/scala/RunWebApp.scala") :: 
		TemplateFile("%s/basic-lift-project/resources/props/default.props".format(GlobalConfiguration.rootResources),"src/main/resources/props/default.props") ::
    TemplateFile("%s/basic-lift-project/webapp/templates-hidden/default.html".format(GlobalConfiguration.rootResources),"src/main/webapp/templates-hidden/default.html") ::
    TemplateFile("%s/basic-lift-project/webapp/templates-hidden/wizard-all.html".format(GlobalConfiguration.rootResources),"src/main/webapp/templates-hidden/wizard-all.html") :: 
    TemplateFile("%s/basic-lift-project/webapp/WEB-INF/web.xml".format(GlobalConfiguration.rootResources),"src/main/webapp/WEB-INF/web.xml") :: 
    TemplateFile("%s/blank-lift-project/index-blank.html".format(GlobalConfiguration.rootResources),"src/main/webapp/index.html") :: 
		TemplateFile("%s/blank-lift-project/boot.ssp".format(GlobalConfiguration.rootResources),"src/main/scala/bootstrap/liftweb/Boot.scala") :: 
		TemplateFile("%s/basic-lift-project/test/AppTest.ssp".format(GlobalConfiguration.rootResources),"src/test/scala/${pack}/AppTest.scala") :: 
		Nil
	}
  
  override def postRenderAction(arguments: List[ArgumentResult]): Unit = {
    createFolderStructure(arguments)(LiftHelper.liftFolderStructure :_*)    
  }
  
  object pack extends PackageArgument("pack") with Default with Value { value = defaultMainPackage }
  
  lazy val defaultMainPackage = searchForMainPackage match {
	  case Full(packageName) => Full(packageName)
    case Empty => Empty
    case Failure(msg,_,_) => Failure(msg)
  }
  
}

object LiftProjectTemplate extends DefaultLiftTemplate {
	
	override def dependencies = BlankLiftProject :: UserTemplate :: DependencyFactory :: Nil
	
	def name = "project"
	
	def description = "Creates a Lift project with some functionality to get you started"
	
	def arguments = mainPackage :: Nil
	
	override def postRenderAction(arguments: List[ArgumentResult]): Unit = {
		createFolderStructure(arguments)(LiftHelper.liftFolderStructure :_*)
	}
		
	def files = {
		TemplateFile("%s/ProjectDefinition.scala".format(basePath),"project/build/Project.scala") :: 
		TemplateFile("%s/webapp/static/index.html".format(basePath),"src/main/webapp/static/index.html") :: 
		TemplateFile("%s/basic-lift-project/index-basic.html".format(GlobalConfiguration.rootResources),"src/main/webapp/index.html") :: 
		TemplateFile("%s/boot.ssp".format(basePath),"src/main/scala/bootstrap/liftweb/Boot.scala") :: 
		TemplateFile("%s/helloworld.ssp".format(basePath),"src/main/scala/${pack}/snippet/HelloWorld.scala") :: 
		TemplateFile("%s/test/HelloWorldTest.ssp".format(basePath),"src/test/scala/${pack}/snippet/HelloWorldTest.scala") :: 
		Nil
	}
	
	// stuff to keep it dry
	
	object mainPackage extends PackageArgument("pack") with Default with Value { value = defaultMainPackage }
	
	val basePath = "%s/basic-lift-project".format(GlobalConfiguration.rootResources)
	
	lazy val defaultMainPackage = searchForMainPackage match {
	  case Full(packageName) => Full(packageName)
    case Empty => Empty
    case Failure(msg,_,_) => Failure(msg)
  }
}