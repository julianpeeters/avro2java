package com.julianpeeters.avro2java.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object InputSubmitComponent {

  type OnChange = (ReactEventFromInput) => Callback
  type OnSubmit = (ReactEventFromInput) => Callback

  case class Props(onChange: OnChange,
                   onSubmit: OnSubmit,
                   inputText: String,
                   enabled: Boolean)

  val component = ScalaComponent
    .builder[Props]("InputSubmitComponent")
    .render_P(props =>
      <.div(
        <.div(
        <.textarea(
          ^.placeholder := "paste your schema here",
          ^.value := props.inputText,
          ^.onChange ==> props.onChange,
          ^.disabled := !props.enabled
        )),<.div(
        <.input(
          ^.value := "Compile",
          ^.`type` := "button",
          ^.onClick ==> props.onSubmit,
          ^.disabled := !props.enabled || props.inputText.isEmpty
        )
      ))
    )
    .build
}
