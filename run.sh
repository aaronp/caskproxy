#/usr/env/bin bash

# scala-cli if you don't already have it ...
which scala-cli || brew install Virtuslab/scala-cli/scala-cli

# The only file we're going to create is ./ui/index.html!
mkdir ui && echo '<html><body>Hypertext Markup Language is da bomb!</body></html>' > ui/index.html

# run your web app - serve some static files, some server-side rendering, etc. 
cat <<EOF | scala-cli -
//> using scala "3.1.3"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.11.1"

import scalatags.Text.all._

object MyApp extends cask.MainRoutes {
  
  @cask.get("/")
  def index() = cask.Redirect("/ui/index.html")

  @cask.staticFiles("/ui", headers = Seq("content-type" -> "text/html"))
  def staticFileRoutes() = "ui"

  @cask.get("/server-side")
  def hello() = {
    doctype("html")(
      html(
        body(
          h1("Server-side rendering"),
          p("... if that's your thing")
        )
      )
    )
  }


  override def host: String = "0.0.0.0"
  override def port = 8080

  initialize()
}
MyApp.main(Array.empty)

println(s"Serving http://localhost:8080")
java.awt.Desktop.getDesktop.browse(new java.net.URI("http://localhost:8080"))
EOF