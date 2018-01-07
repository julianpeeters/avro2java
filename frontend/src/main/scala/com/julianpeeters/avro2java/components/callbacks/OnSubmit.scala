package com.julianpeeters.avro2java.components
package callbacks

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import japgolly.scalajs.react._
import org.scalajs.dom.ext.Ajax
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object OnSubmit {
  def modify(
    scope: BackendScope[Unit, AppComponent.State],
    state: AppComponent.State): Callback = {
    val beginSubmitting =
      scope.modState(_.copy(submittingInput = true))
    val finishSubmitting =
      scope.modState(_.copy(submittingInput = false))
    val io = CallbackTo.future {
      Ajax.post("/", state.input)
          .map(r  => decode[List[String]](r.responseText))
          .map(rs => scope.modState(_.copy(results = rs.getOrElse(Nil))))
    }
    beginSubmitting >> io >> finishSubmitting
  }
}