//> using scala "3.2.0"
//> using lib "com.lihaoyi::cask:0.8.3"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//> using lib "com.softwaremill.sttp.client3::core:3.8.3"

import _root_.sttp.client3.quick.*
import cask.model.Cookie
import scalatags.Text.all.*
import sttp.client3.{Request, Response}
import sttp.model.Uri

import java.nio.channels.UnresolvedAddressException
import java.time.Instant

object ProxyServer extends cask.MainRoutes {

  @cask.route("/", methods = Seq("get", "post", "put", "options", "delete"), subpath = true)
  def onProxyRoute(request: cask.Request) = {
    val proxyRequest = request.asRequest
    println(s"Sending proxy request:\n$proxyRequest")
    try {
      val sttpResponse: Response[String] = simpleHttpClient.send(proxyRequest)
      println(s"Got proxy response:\n$sttpResponse")
      sttpResponse.asCaskResponse
    } catch {
      case e: UnresolvedAddressException =>
        println(s"Got unresolved address for ${proxyRequest}")
        cask.Response(s"UnresolvedAddress for $proxyRequest:\n ${e}\n${e.getStackTrace.mkString("\n")}", 500)
      case other =>
        other.printStackTrace()
        cask.Response(s"Bang: ${other}\n${other.getStackTrace.mkString("\n")}", 500)
    }
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


