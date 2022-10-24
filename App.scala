//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

import scalatags.Text.all.*
import _root_.sttp.client3.quick.*

object App extends cask.MainRoutes {

  @cask.get("/")
  def index() = cask.Redirect("/ui/index.html")

  @cask.get("/web")
  def webRoute() = cask.Redirect("/web/game.html")

  @cask.staticFiles("/ui", headers = Seq("content-type" -> "text/html", "Cache-Control" -> "max-age=31536000"))
  def staticFileRoutes() = "ui"

  @cask.staticFiles("/web", headers = Seq("content-type" -> "text/html", "Cache-Control" -> "max-age=31536000"))
  def webRoutes() = "web"

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


  private def box(str : String): String = {
    val lines = str.linesIterator.toList
    val maxLen = (0 +: lines.map(_.length)).max
    val boxed = lines.map { line =>
      s" | ${line.padTo(maxLen, ' ')} |"
    }
    val bar = " +-" + ("-" * maxLen) + "-+"
    (bar +: boxed :+ bar).mkString("\n")
  }

  override def host: String = "0.0.0.0"
  override def port = 8070

  locally {
    initialize()
    println(box(
      s""" 🚀 browse to localhost:$port and/or open jconsole 🚀
         |      host : $host
         |      port : $port
         |   verbose : $verbose
         | debugMode : $debugMode
         |""".stripMargin))
    java.awt.Desktop.getDesktop.browse(new java.net.URI(s"http://localhost:${port}"))
  }
}


