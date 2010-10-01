Lifty
=====

Lifty is part of the [Lifty project](http://lifty.org "Lifty project").

Lifty is a [SBT processor](http://code.google.com/p/simple-build-tool/wiki/Processors "SBT processor") for generating files associated with the Lift framework. This was developed as part of my Google Summer of Code 2010 project. It's using a framework that I created, see: [Lifty Engine](http://github.com/Lifty/Lifty-engine/ "Lifty Engine").

For more information check out it's [Github page](http://lifty.github.com/Lifty/ "Github page").

How do I use it?
================

If you want to try out the processor start SBT and run the following

<pre><code>*ScalaToolsSnapshot at http://scala-tools.org/repo-snapshots/
*lifty is org.lifty lifty 1.2
</code></pre>

If you want to build it locally simply run the following (SBT is needed)

<pre><code>git clone git@github.com:Lifty/Lifty.git
cd Lifty
sbt
update
compile</code></pre>