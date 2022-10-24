//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

import scalatags.Text.all.*
import _root_.sttp.client3.quick.*
import _root_.sttp.client3.*

object App extends cask.MainRoutes {

  private def defaultProxyHost = sys.env.get("PROXY").getOrElse {
    sys.error("proxyHost was not specified as a query parameter and env 'PROXY' is not set")
  }

  def chomp(ending : String)(str : String) =
    str match {
      case _ if str.endsWith(ending) => str.substring(0, str.length - ending.length)
      case _ => str
    }

  extension (request: cask.Request)
    def method = request.exchange.getRequestMethod
    def isDelete = method.equalToString("delete")
    def isOption = method.equalToString("option")
    def isPut = method.equalToString("put")
    def isPost = method.equalToString("post")
    def isGet = method.equalToString("get")

    def asRequestUri =
      val full = request.remainingPathSegments.mkString(proxyHost, "/", "")
      uri"${full}"

    def requestBody = request.bytes

    def asRequest: Request[String, Any] =
      val request: Request[String, Any] = method.toString.toLowerCase.trim match {
        case "get" => quickRequest.get(asRequestUri)
        case "post" => quickRequest.post(asRequestUri)
        case "put" => quickRequest.put(asRequestUri)
        case "delete" => quickRequest.delete(asRequestUri)
        case "options" => quickRequest.options(asRequestUri)
        case other => sys.error(s"BUG: unknown method type '${other}'")
      }

      request.body(request.bytes).headers(request.headers.toMap)

    def proxyHost = chomp("/") {
      request.queryParams.get("proxyHost") match {
        case Some(proxyHost) =>
        case None => defaultProxyHost
      }
    }

  def doProxy() = {
    val got = simpleHttpClient.send(quickRequest.get(uri"http://httpbin.org/ip"))
    got.toString
  }

  @cask.get("/test")
  def test() = {
    val got = simpleHttpClient.send(quickRequest.get(uri"http://httpbin.org/ip"))
    got.toString
  }

  @cask.route("/", methods = Seq("get", "post", "put", "options", "delete"), subpath = true)
  def onProxyRoute(request: cask.Request) = {

    request.asRequestUri

    println(s"Handling $request")
    if (request.exchange.getRequestMethod.equalToString("post")) "do_the_login"
    else "show_the_login_form"
  }

  @cask.get("/home")
  def index() = cask.Redirect("/ui/index.html")

  @cask.get("/game")
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


