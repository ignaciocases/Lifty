package com.sidewayscoding.specs

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.sidewayscoding.LiftHelper

class HelperTest extends FlatSpec with ShouldMatchers {
  
  "LiftHelper" should "be able to find the package name in Boot.scala" in {
    val packageName = LiftHelper.searchForPackageInBoot("src/test/resources/Boot.scala").open_!
    packageName should be === "com.sidewayscoding"
  }
  
  
  
}