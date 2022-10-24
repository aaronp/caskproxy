//> using platform "scala-js"
//> using scala "3.2.0"
//> using lib "org.scala-js::scalajs-dom:2.3.0"
//> using lib "com.lihaoyi::scalatags:0.12.0"
//
/**
 * Usage:
 * {{{
 *   scala-cli package --js web.scala -o hello.js --js-emit-source-maps
 * }}}
 *
 * see https://scala-cli.virtuslab.org/docs/guides/scala-js
 * scala-cli setup-ide .
 * scala-cli . --js
 */
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.annotation.*
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.{Ajax, AjaxException}

import java.util.Base64
import concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.URIUtils.encodeURIComponent

def appendPar(targetNode: dom.Node, text: String): Unit = {
  val parNode = document.createElement("p")
  parNode.textContent = text
  targetNode.appendChild(parNode)
}

@JSExportTopLevel("addClickedMessage")
def addClickedMessage(): Unit = {
  appendPar(document.body, "You clicked the button!")
}


@JSExportTopLevel("App")
object App {
  @JSExport("run")
  def run() = main()
}

@main def main() = {
  document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
    setupUI()
  })
}

def setupUI(): Unit = {
  val button = document.createElement("button")
  button.textContent = "Click me!"
  button.addEventListener("click", { (e: dom.MouseEvent) =>
    addClickedMessage()
  })
  document.body.appendChild(button)

  appendPar(document.body, "Hello World")
}