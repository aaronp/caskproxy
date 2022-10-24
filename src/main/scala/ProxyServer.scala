//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

import _root_.sttp.client3.quick.*
import scalatags.Text.all.*
import sttp.client3.Request
import sttp.model.Uri

object ProxyServer extends cask.MainRoutes {

  @cask.route("/", methods = Seq("get", "post", "put", "options", "delete"), subpath = true)
  def onProxyRoute(request: cask.Request) = {
    val proxyRequest = request.asRequest
    println(s"Sending proxy request:\n$proxyRequest")
    val got = simpleHttpClient.send(proxyRequest)
    println(s"Got proxy response:\n$got")
    got.toString
  }

  override def host: String = "0.0.0.0"

  override def port = sys.env.get("PORT").map(_.toInt).getOrElse(8070)

  initialize()
  println(box(
    s""" 🚀 browse to localhost:$port and/or open jconsole 🚀
       |      host : $host
       |      port : $port
       |   verbose : $verbose
       | debugMode : $debugMode
       |""".stripMargin))
}


