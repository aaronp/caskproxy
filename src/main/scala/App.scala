//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

//import _root_.sttp.client3.*
import _root_.sttp.client3.quick.*
import scalatags.Text.all.*
import sttp.client3.Request
import sttp.model.Uri

object App extends cask.MainRoutes {

  @cask.get("/home")
  def index() = cask.Redirect("/ui/index.html")

  @cask.get("/game")
  def webRoute() = cask.Redirect("/web/game.html")

  @cask.staticFiles("/ui", headers = Seq("content-type" -> "text/html", "Cache-Control" -> "max-age=31536000"))
  def staticFileRoutes() = "../../../ui"

  @cask.staticFiles("/web", headers = Seq("content-type" -> "text/html", "Cache-Control" -> "max-age=31536000"))
  def webRoutes() = "../../../web"

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

  override def port = sys.env.get("PORT").map(_.toInt).getOrElse(8070)

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


