import _root_.sttp.client3.quick.*
import scalatags.Text.all.*
import sttp.client3.Request
import sttp.model.Uri

private def defaultProxyHost = sys.env.get("PROXY").getOrElse {
  sys.error("proxyHost was not specified as a query parameter and env 'PROXY' is not set")
}

def box(str: String): String = {
  val lines = str.linesIterator.toList
  val maxLen = (0 +: lines.map(_.length)).max
  val boxed = lines.map { line =>
    s" | ${line.padTo(maxLen, ' ')} |"
  }
  val bar = " +-" + ("-" * maxLen) + "-+"
  (bar +: boxed :+ bar).mkString("\n")
}


def chomp(ending: String)(str: String) =
  str match {
    case _ if str.endsWith(ending) => str.substring(0, str.length - ending.length)
    case _ => str
  }


extension (request: cask.Request)
  def method = request.exchange.getRequestMethod

  def asRequestUri: Uri =
    val full = request.remainingPathSegments.mkString(proxyHost, "/", "")
    val uriFull = uri"${full}"
    println(s"uriFull: >$uriFull<, request.remainingPathSegments=${request.remainingPathSegments}, method:${method}, getRequestURI:${request.exchange.getRequestURI}, getRequestPath:${request.exchange.getRequestPath}, relativePath:${request.exchange.getRelativePath}, resolved:${request.exchange.getResolvedPath}, requestURL:${request.exchange.getRequestURL}")
    uriFull

  def headersMap: Map[String, String] =
    val map = request.headers.view.mapValues(_.mkString(",")).toMap
    val restrictedHeaders = Set("connection", "content-length", "expect", "host", "upgrade")
    map.removedAll(restrictedHeaders)

  def asRequest: Request[String, Any] =
    val proxyRequest: Request[String, Any] = method.toString.toLowerCase.trim match {
      case "get" => quickRequest.get(asRequestUri)
      case "post" => quickRequest.post(asRequestUri)
      case "put" => quickRequest.put(asRequestUri)
      case "delete" => quickRequest.delete(asRequestUri)
      case "options" => quickRequest.options(asRequestUri)
      case other => sys.error(s"BUG: unknown method type '${other}'")
    }
    proxyRequest.body(request.bytes).headers(headersMap).maxRedirects(10)

  def proxyHost: String = chomp("/") {
    request.queryParams.get("proxyHost") match {
      case Some(Seq(proxyHost)) => proxyHost.toString
      case Some(many) => sys.error(s"${many.size} proxy hosts specified: $many")
      case None => defaultProxyHost
    }
  }