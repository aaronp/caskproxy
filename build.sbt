scalaVersion := "3.2.0"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "cask" % "0.8.3" ,
  "com.lihaoyi" %% "scalatags" % "0.12.0" ,
  "com.softwaremill.sttp.client3" %% "core" % "3.8.3" 
)

sourceDirectory := new java.io.File(".")