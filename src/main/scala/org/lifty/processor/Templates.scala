package org.lifty.processor

import sbt._
import sbt.processor.BasicProcessor
import org.lifty.engine._
import org.lifty.util.TemplateHelper._
import org.lifty.processor.LiftHelper._
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

object Layout extends DefaultLiftTemplate {
  
  def name = "layout"
  
  def description = "Creates an empty layout with a surround tag"
  
  def arguments = Argument("layoutName") :: Nil
  
  def files = TemplateFile(
    "%s/layout.ssp".format(GlobalConfiguration.rootResources),
    "src/main/webapp/${layoutName}.html"
  ) :: Nil   
}

/**
* Template for a model class implemented using Mapper. It provides basic functionality
* that would expect from a User.
*/
object UserTemplate extends DefaultLiftTemplate {
  
  def name = "user"
  
  def description = "Template for a model class implemented using Mapper. It provides basic functionality that would expect from a User"
  
  def arguments = pack :: Nil
  
  val path = "%s/user".format(GlobalConfiguration.rootResources)	
  
  def files = TemplateFile(
    "%s/user.ssp".format(path),
    "src/main/scala/${modelpack}/User.scala"
  ) :: Nil
  
  injectContentsOfFile("%s/boot_import_injections_user.ssp".format(path)) into("boot.ssp") at("imports")
  injectContentsOfFile("%s/boot_sitemap_injections_user.txt".format(path)) into("boot.ssp") at("sitemap")
  injectContentsOfFile("%s/boot_bottom_injections_user.txt".format(path)) into("boot.ssp") at("bottom")
  injectContentsOfFile("%s/boot_top_injections_user.txt".format(path)) into("boot.ssp") at("top")
  injectContentsOfFile("%s/ProjectDefinition_dependencies_injections_user.txt".format(path)) into("Project.ssp") at("dependencies")	
  
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
    TemplateFile("%s/Project.ssp".format(blankProjectPath),"project/build/Project.scala") ::
    TemplateFile("%s/LiftConsole.scala".format(blankProjectPath),"src/test/scala/LiftConsole.scala") :: 
		TemplateFile("%s/RunWebApp.scala".format(blankProjectPath),"src/test/scala/RunWebApp.scala") :: 
		TemplateFile("%s/default.props".format(blankProjectPath),"src/main/resources/props/default.props") ::
    TemplateFile("%s/default.html".format(blankProjectPath),"src/main/webapp/templates-hidden/default.html") ::
    TemplateFile("%s/404.html".format(blankProjectPath),"src/main/webapp/404.html") :: 
    TemplateFile("%s/wizard-all.html".format(blankProjectPath),"src/main/webapp/templates-hidden/wizard-all.html") :: 
    TemplateFile("%s/web.xml".format(blankProjectPath),"src/main/webapp/WEB-INF/web.xml") :: 
    TemplateFile("%s/index.ssp".format(blankProjectPath),"src/main/webapp/index.html") :: 
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
	
	val path = "%s/basic-lift-project".format(GlobalConfiguration.rootResources)	
	
	def files = {
		TemplateFile("%s/index-static.html".format(path),"src/main/webapp/static/index.html") :: 
		TemplateFile("%s/helloworld.ssp".format(path),"src/main/scala/${mainpack}/snippet/HelloWorld.scala") :: 
		TemplateFile("%s/HelloWorldTest.ssp".format(path),"src/test/scala/${mainpack}/snippet/HelloWorldTest.scala") :: 
		Nil
	}
	
	injectContentsOfFile("%s/boot_import_injections.ssp".format(path)) into("boot.ssp") at("imports")
	injectContentsOfFile("%s/boot_bottom_injections.ssp".format(path)) into("boot.ssp") at("bottom")
	injectContentsOfFile("%s/boot_sitemap_injections.ssp".format(path)) into("boot.ssp") at("sitemap")
	injectContentsOfFile("%s/index_content_injections.ssp".format(path)) into("index.ssp") at("content")
	injectContentsOfFile("%s/ProjectDefinition_dependencies_injections.ssp".format(path)) into("Project.ssp") at("dependencies")	
	
	object mainPackage extends PackageArgument("mainpack") with Default with Value { value = defaultMainPackage }

}