package com.julianpeeters.avro2java.components

import com.julianpeeters.avro2java.components.callbacks.{OnChange, OnSubmit}

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object AppComponent {

  case class State(
    input: String,
    results: List[String],
    submittingInput: Boolean)
  object State {
    val initial = State("", Nil, false)
  }
                   
  class Backend(scope: BackendScope[Unit, State]) {
    def render(state: State): VdomElement =
      <.div(
        inputSubmit(state),
        resultList(state))

    private def resultList(state: State) =
      ResultListComponent.component(ResultListComponent.Props(state.results))

    private def inputSubmit(state: State) = {
      InputSubmitComponent.component(
        InputSubmitComponent.Props(
          onChange = OnChange.setState(scope),
          onSubmit = _ => OnSubmit.modify(scope, state),
          inputText = state.input,
          enabled = !state.submittingInput
         )
       )
    }
    
  }
  
  val component = ScalaComponent
    .builder[Unit]("AppComponent")
    .initialState(State.initial)
    .renderBackend[Backend]
    .build
}