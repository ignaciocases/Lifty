SBT Sample Processor
====================

This is a [SBT processor](http://code.google.com/p/simple-build-tool/wiki/Processors "SBT processor") for generating files associated with the Lift framework. This was developed as part of my Google Summer of Code 2010 project. It's using a framework that I created, see: [Simple-Build-Tool-Template-Engine](http://github.com/mads379/Simple-Build-Tool-Template-Engine "Simple-Build-Tool-Template-Engine").

For more information check out it's [Github page](http://mads379.github.com/Lift-sbt-processor/ "Github page").

If you want to try out the processor start SBT and run the following

<pre><code>*ScalaToolsSnapshot at http://scala-tools.org/repo-snapshots/
*lift is com.sidewayscoding lift_processor 1.1
</code></pre>

If you want to build it locally simply run the following (SBT is needed)

<pre><code>git clone git@github.com:mads379/Lift-sbt-processor.git
cd Lift-sbt-processor
sbt
update
compile</code></pre>