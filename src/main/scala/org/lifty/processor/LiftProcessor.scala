package org.lifty.processor

import sbt._
import org.lifty.engine._

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
