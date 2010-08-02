package com.sidewayscoding

import sbt._
import sbt.processor.BasicProcessor
import template.engine._
import net.liftweb.common._

/**
* The lift processor. We simply give it the templates taht we want it to create
* be able to create
* 
*/
class LiftProcessor extends SBTTemplateProcessor {
	def templates = List(
	  SnippetTemplate,
	  MapperTemplate,
	  CometTemplate,
	  Layout,
	  LiftProjectTemplate,
	  BlankLiftProject,
	  UserTemplate,
	  DependencyFactory)
}

object LiftProcessor extends LiftProcessor {}
