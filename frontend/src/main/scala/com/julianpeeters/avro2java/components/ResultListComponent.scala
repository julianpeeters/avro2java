package com.julianpeeters.avro2java.components

import japgolly.scalajs.react._

import japgolly.scalajs.react.vdom.html_<^._

object ResultListComponent {

  case class Props(results: List[String])

  val component = ScalaComponent
    .builder[Props]("ResultsComponent")
    .render_P(
      props =>
        <.div(
          {if (props.results.isEmpty)
            List(<.textarea(^.placeholder := "here is the result"))
          else props.results.map(result =>
            <.textarea(^.value := result))
          }: _*)
      )
    .build

}
