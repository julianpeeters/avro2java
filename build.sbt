lazy val commonSettings = {
  organization := "com.julianpeeters"
  version := "0.0.1-SNAPSHOT"
  scalaVersion := "2.12.4"
}

val Http4sV = "0.18.0-M8"
val utestV = "0.6.2"
val scalaJsDomV = "0.9.4"
val circeV = "0.9.0"
val fs2V = "0.10.0-M11"

// This function allows triggered compilation to run only when scala files changes
// It lets change static files freely
def includeInTrigger(f: java.io.File): Boolean =
  f.isFile && {
    val name = f.getName.toLowerCase
    name.endsWith(".scala") || name.endsWith(".js")
  }

lazy val shared =
  (crossProject.crossType(CrossType.Pure) in file("shared"))
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "scalatags" % "0.6.5",
        "io.circe"    %%% "circe-generic" % "0.7.1"
        //"org.http4s"     %%% "http4s-circe"        % Http4sV

      )
    )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val backend = (project in file("backend"))
  .settings(
    name := "avro2java-backend"
  )
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.julianpeeters" %% "avrohugger-core" % "0.18.0",
      "org.http4s"     %% "http4s-blaze-server" % Http4sV,
      "org.http4s"     %% "http4s-circe"        % Http4sV,
      "org.http4s"     %% "http4s-dsl"          % Http4sV,
      "ch.qos.logback" % "logback-classic"      % "1.2.3"
    ),
    // Allows to read the generated JS on client
    resources in Compile += (fastOptJS in (frontend, Compile)).value.data,
    // Lets the backend to read the .map file for js
    resources in Compile += (fastOptJS in (frontend, Compile)).value
      .map((x: sbt.File) => new File(x.getAbsolutePath + ".map"))
      .data,
    // Lets the server read the jsdeps file
    (managedResources in Compile) += (artifactPath in (frontend, Compile, packageJSDependencies)).value,
    // do a fastOptJS on reStart
    reStart := (reStart dependsOn (fastOptJS in (frontend, Compile))).evaluated,
    // This settings makes reStart to rebuild if a scala.js file changes on the client
    watchSources ++= (watchSources in frontend).value,
    // Support stopping the running server
    mainClass in reStart := Some("com.julianpeeters.avro2java.Server")
  )
  .dependsOn(sharedJvm)

lazy val frontend = (project in file("frontend"))
  .settings(
    name := "avro2java-frontend"
  )
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    // Requires the DOM
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv,
    // Build a js dependencies file
    skip in packageJSDependencies := false,
    // Put the jsdeps file on a place reachable for the server
    crossTarget in (Compile, packageJSDependencies) := (resourceManaged in Compile).value,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "co.fs2"       %%% "fs2-core"      % fs2V,
      "com.lihaoyi"  %%% "utest"         % utestV % Test,
      "io.circe"     %%% "circe-core"    % circeV,
      "io.circe"     %%% "circe-generic" % circeV,
      "io.circe"     %%% "circe-parser"  % circeV,
      "org.scala-js" %%% "scalajs-dom"   % scalaJsDomV,
    )
  )
  .dependsOn(sharedJs)
