package com.sidewayscoding

import LiftHelper._
import sbt._
import sbt.processor.BasicProcessor

import template.engine._
import template.util.TemplateHelper._
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
	  LiftProjectTemplate,
	  BlankLiftProject,
	  UserTemplate,
	  DependencyFactory)
}

object LiftProcessor extends LiftProcessor {}
