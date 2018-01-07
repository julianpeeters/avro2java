package com.julianpeeters.avro2java.components
package callbacks

import japgolly.scalajs.react._

object OnChange {
  def setState(scope: BackendScope[Unit, AppComponent.State])(e: ReactEventFromInput): Callback =
    e.extract(_.target.value)(value =>
      scope.modState(_.copy(input = value)))
}