package org.lifty.processor

import sbt._
import org.lifty.engine._

// yeah yeah, I know. Copy-Past coind going on right here.
object LiftyStandAlone extends StandAloneTemplateProcessor {
    
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
