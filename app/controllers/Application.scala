package controllers

import scala.util._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._
import play.api.mvc._
import play.api.libs.ws.WS

object Application extends Controller {

    def index = Action { request =>
        val hostname = request.host

        Ok(views.html.index(hostname))
    }

    def player(resource: String) = Action {
        val filepath = "http://bit.ly/" + resource

        Async {
            val fResp = WS.url(filepath).withFollowRedirects(false).head

            fResp.map { resp =>
                println("Status: "+resp.status)
                println(resp.ahcResponse.getHeaders())
                val resolved = resp.header("Location").getOrElse(filepath)
                Ok(views.html.player(resolved))
            }
        }
    }

}