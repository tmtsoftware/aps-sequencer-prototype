import kotlin.Keys._

val KotlincOptions = Seq(
  "-opt-in=kotlin.time.ExperimentalTime",
  "-Xallow-any-scripts-in-source-roots",
  "-Xuse-fir-lt=false",
  "-jvm-target",
  "21"
)
val KotlinVersion = "2.1.10"

// -----------------------------------------------------------------------------
// scripts subproject: compiles Kotlin .kts scripts and bundles conf resources
// -----------------------------------------------------------------------------
lazy val scripts = project
  .in(file("scripts"))
  .enablePlugins(KotlinPlugin)
  .settings(
    name             := "aps-scripts",
    organization     := "org.tmt.aps",
    scalaVersion     := "3.6.4",
    version          := "0.1.0-SNAPSHOT",
    kotlinVersion    := KotlinVersion,
    kotlincJvmTarget := "21",
    kotlincOptions ++= KotlincOptions,
    kotlinLib("stdlib"),
    resolvers += "jitpack" at "https://jitpack.io",
    // .kts files are sources; .conf files are resources
    Compile / unmanagedSourceDirectories  := Seq(baseDirectory.value),
    Compile / unmanagedSources / excludeFilter := "*.conf",
    Compile / unmanagedResourceDirectories := Seq(baseDirectory.value),
    Compile / unmanagedResources / includeFilter := "*.conf",
    libraryDependencies ++= Seq(
      Libs.`esw-ocs-dsl-kt`,
      Libs.`esw-ocs-app`
    )
  )

// -----------------------------------------------------------------------------
// runner subproject: plain Scala, starts the sequencer and keeps JVM alive
// -----------------------------------------------------------------------------
lazy val runner = project
  .in(file("runner"))
  .dependsOn(scripts)
  .settings(
    name         := "aps-runner",
    organization := "org.tmt.aps",
    scalaVersion := "3.6.4",
    version      := "0.1.0-SNAPSHOT",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      Libs.`esw-ocs-app`
    ),
    Compile / mainClass := Some("aps.SequencerMain"),
    fork := true
  )
