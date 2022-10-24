enablePlugins(ScalaJSPlugin)

name := "client"
scalaVersion := "3.2.0"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.1.0"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.12.0"

// for the game
libraryDependencies += "io.indigoengine" %%% "indigo" % "0.14.0"
libraryDependencies += "io.indigoengine" %%% "indigo-extras" % "0.14.0"

