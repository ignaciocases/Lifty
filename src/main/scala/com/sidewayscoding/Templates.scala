package com.sidewayscoding

import LiftHelper._
import sbt._
import sbt.processor.BasicProcessor

import template.engine._
import template.util.TemplateHelper._
import net.liftweb.common._

trait DefaultLiftTemplate extends Template with Create with Delete{
  lazy val defaultMainPackage = searchForMainPackage match {
	  case Full(packageName) => Full(packageName)
    case Empty => Empty
    case Failure(msg,_,_) => Failure(msg)
  }
}

object SnippetTemplate extends DefaultLiftTemplate {
	
	def name = "snippet"
	
	def description = "Creates a snippet"
	
	def arguments = {
		object packageArgument extends PackageArgument("snippetpack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".snippet"))
		}
		Argument("snippetName") :: packageArgument ::  Nil
	}
	
	def files = {
	  val templatePath = "%s/snippet.ssp".format(GlobalConfiguration.rootResources)
	  val snippetPath = "src/main/scala/${snippetpack}/${snippetName}.scala"
	  TemplateFile(templatePath,snippetPath) :: Nil
	}
}
	
object MapperTemplate extends DefaultLiftTemplate {
	
	def name = "mapper"
	
	def description = "Creates a model class using Mapper"
	
	override def notice(argResults: List[ArgumentResult]) = 
	  Full(replaceVariablesInPath("Remember to add ${modelName} to the Schemifier in your boot.scala file", argResults))
		
	def arguments = {
		object packageArgument extends PackageArgument("modelpack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".model"))
		}
		object nameArgument extends Argument("modelName")
		object fieldArgument extends Argument("fields") with Repeatable with Optional
  	
  	nameArgument :: packageArgument :: fieldArgument :: Nil
	}
	
  def files = {
    val templatePath = "%s/mapper.ssp".format(GlobalConfiguration.rootResources)
    val mapperPath = "src/main/scala/${modelpack}/${modelName}.scala"
    TemplateFile(templatePath,mapperPath) :: Nil
  }
}

object CometTemplate extends DefaultLiftTemplate {
	
	def name = "comet"
	
	def description = "Creates a comet component"
	
	override def notice(argResults: List[ArgumentResult]) = 
	  Full(replaceVariablesInPath("Add <lift:comet type='${cometName}' /> in a template file to use the comet component", argResults))
	
	def arguments = {
		object packageArgument extends PackageArgument("cometpack") with Default with Value {
		  value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".comet"))
		}
		Argument("cometName") :: packageArgument ::  Nil
	}
	
	def files = {
	  val templatePath = "%s/comet.ssp".format(GlobalConfiguration.rootResources)
	  val snippetPath = "src/main/scala/${cometpack}/${cometName}.scala"
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
    "%s/user.ssp".format(GlobalConfiguration.rootResources),
    "src/main/scala/${modelpack}/User.scala"
  ) :: Nil
  
  object pack extends PackageArgument("modelpack") with Default with Value { 
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
    "%s/dependencyFactory.ssp".format(GlobalConfiguration.rootResources),
    "src/main/scala/${libpack}/DependencyFactory.scala"
  ) :: Nil
  
  object pack extends PackageArgument("libpack") with Default with Value { 
    value = searchForPackageInBoot("src/main/scala/bootstrap/liftweb/Boot.scala",Full(".lib"))
  }
  
}

object BlankLiftProject extends DefaultLiftTemplate {
  
  def name = "project-blank"
  
  def description = "Creates a blank Lift project that uses SBT as it's build system"
  
  def arguments = pack :: Nil
  
  def files = {
    val blankProjectPath = "%s/blank-lift-project".format(GlobalConfiguration.rootResources)
    TemplateFile("%s/ProjectDefinition.scala".format(blankProjectPath),"project/build/Project.scala") ::
    TemplateFile("%s/LiftConsole.scala".format(blankProjectPath),"src/test/scala/LiftConsole.scala") :: 
		TemplateFile("%s/RunWebApp.scala".format(blankProjectPath),"src/test/scala/RunWebApp.scala") :: 
		TemplateFile("%s/default.props".format(blankProjectPath),"src/main/resources/props/default.props") ::
    TemplateFile("%s/default.html".format(blankProjectPath),"src/main/webapp/templates-hidden/default.html") ::
    TemplateFile("%s/wizard-all.html".format(blankProjectPath),"src/main/webapp/templates-hidden/wizard-all.html") :: 
    TemplateFile("%s/web.xml".format(blankProjectPath),"src/main/webapp/WEB-INF/web.xml") :: 
    TemplateFile("%s/index-blank.html".format(blankProjectPath),"src/main/webapp/index.html") :: 
		TemplateFile("%s/boot.ssp".format(blankProjectPath),"src/main/scala/bootstrap/liftweb/Boot.scala") :: 
		TemplateFile("%s/AppTest.ssp".format(blankProjectPath),"src/test/scala/${mainpack}/AppTest.scala") :: 
		Nil
	}
  
  override def postRenderAction(arguments: List[ArgumentResult]): Unit = {
    createFolderStructure(arguments)(LiftHelper.liftFolderStructure :_*)    
  }
  
  object pack extends PackageArgument("mainpack") with Default with Value { value = defaultMainPackage }
  
}

object LiftProjectTemplate extends DefaultLiftTemplate {
	
	override def dependencies = BlankLiftProject :: UserTemplate :: DependencyFactory :: Nil
	
	def name = "project"
	
	def description = "Creates a Lift project with some functionality to get you started"
	
	def arguments = mainPackage :: Nil
	
	override def postRenderAction(arguments: List[ArgumentResult]): Unit = {
		createFolderStructure(arguments)(LiftHelper.liftFolderStructure :_*)
	}
	
	val basicProjectPath = "%s/basic-lift-project".format(GlobalConfiguration.rootResources)	
	
	def files = {
		//TemplateFile("%s/ProjectDefinition.scala".format(basicProjectPath),"project/build/Project.scala") :: 
		TemplateFile("%s/index-static.html".format(basicProjectPath),"src/main/webapp/static/index.html") :: 
		TemplateFile("%s/index-basic.html".format(basicProjectPath),"src/main/webapp/index.html") :: 
		//TemplateFile("%s/boot.ssp".format(basicProjectPath),"src/main/scala/bootstrap/liftweb/Boot.scala") :: 
		TemplateFile("%s/helloworld.ssp".format(basicProjectPath),"src/main/scala/${mainpack}/snippet/HelloWorld.scala") :: 
		TemplateFile("%s/HelloWorldTest.ssp".format(basicProjectPath),"src/test/scala/${mainpack}/snippet/HelloWorldTest.scala") :: 
		Nil
	}
	
	injectContentsOfFile("%s/boot_import_injections.ssp".format(basicProjectPath)) into("boot.ssp") at("imports")
	injectContentsOfFile("%s/boot_top_injections.ssp".format(basicProjectPath)) into("boot.ssp") at("top")
	injectContentsOfFile("%s/boot_bottom_injections.ssp".format(basicProjectPath)) into("boot.ssp") at("bottom")
	
	// stuff to keep it dry
	
	object mainPackage extends PackageArgument("mainpack") with Default with Value { value = defaultMainPackage }

}