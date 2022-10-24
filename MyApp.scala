//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.1"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

import scalatags.Text.all.*
import _root_.sttp.client3.quick.*

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

  @cask.get("/test")
  def test() = {
    val got = simpleHttpClient.send(quickRequest.get(uri"http://httpbin.org/ip"))
    got.toString
  }


  override def host: String = "0.0.0.0"
  override def port = 8070

  locally {
    initialize()
    println(s"Serving http://localhost:${port}")
    java.awt.Desktop.getDesktop.browse(new java.net.URI(s"http://localhost:${port}"))
  }
}


