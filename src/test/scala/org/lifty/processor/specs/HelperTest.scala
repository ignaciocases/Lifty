package org.lifty.processor.specs

import net.liftweb.common.Empty
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.lifty.processor.LiftHelper

class HelperTest extends FlatSpec with ShouldMatchers {
  
  "LiftHelper" should "be able to find the package name in Boot.scala" in {
    val packageName = LiftHelper.searchForPackageInBoot("src/test/resources/Boot.scala",Empty).open_!
    packageName should be === "com.sidewayscoding"
  }
}