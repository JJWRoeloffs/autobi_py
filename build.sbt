val py4j = "0.10.9.2"

val reflections = "0.9.9-RC1"
val commons_math = "3.3"
val guava = "16.0.1"
val log4j = "1.2.15"
val slf4j_simple = "1.7.7"
val weka = "3.7.5"

val ant_apache_oro = "1.10.13"
val jama = "1.0.3"
val liblinear = "2.44"
val jtransforms = "3.1"
val sspace = "2.0.4"
val scalatest = "3.2.13"
val junit = "4.12"
val junit_interface = "0.11"


ThisBuild / scalaVersion := "3.2.2"

ThisBuild / version := "1.1"

ThisBuild / name         := "autobi_py"
ThisBuild / organization := "edu.leidenuniv"

ThisBuild / resolvers ++= Seq(
  "Brockmann" at "https://www.brockmann-consult.de/mvn/os/",
)

ThisBuild / assemblyMergeStrategy := {
  case PathList(ps @ _*) if ps.last endsWith "module-info.class" => MergeStrategy.concat
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val sharedSettings = Seq(
  javacOptions  ++= Seq("-Xlint", "-encoding", "UTF-8"),
  scalacOptions ++= Seq(
    "-indent",
    "-new-syntax",
    "-feature",
    "-source:future",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:postfixOps",
    "-deprecation",
    "-Xcheck-macros",
  ),
)


lazy val autobi = project
  .in(file("autobi"))
  .settings(
      assembly / mainClass := Some("edu.cuny.qc.speech.AuToBI.AuToBI"),
      name                 := "autobi",
      sharedSettings,
      libraryDependencies ++= Seq(
        "log4j" % "log4j" % log4j,
        "nz.ac.waikato.cms.weka" % "weka-dev" % weka,
        "org.reflections" % "reflections" % reflections,
        "org.apache.commons" % "commons-math3" % commons_math,
        "de.bwaldvogel" % "liblinear" % liblinear,
        "com.google.guava" % "guava" % guava,
        "Jama" % "Jama" % jama,
        "edu.ucla.sspace" % "sspace" % sspace,
        "com.github.wendykierp" % "JTransforms" % jtransforms,
        "org.apache.ant" % "ant-apache-oro" % ant_apache_oro,
        "org.slf4j" % "slf4j-simple" % slf4j_simple % Test,
        "org.scalatest" %% "scalatest" % scalatest % Test,
        "junit" % "junit" % junit % Test,
        "com.novocode" % "junit-interface" % junit_interface % Test,
        ),
      )

 lazy val adapter = project
  .in(file("adapter"))
  .dependsOn(autobi)
  .settings(
    name                          := "adapter",
    assembly / mainClass          := Some("autobiadapter.AutobiServer"),
    // We write the python __init__ with the side-effects of getting the path to write the jar to.
    // Not the cleanest, but it works for now.
    assembly / assemblyOutputPath := file(
      BuildPython
        .getDestenationPathAndWritePython(
          "python/src/autobi",
          (assembly / assemblyJarName).value,
        ),
    ),
    sharedSettings,
    libraryDependencies          ++= Seq(
      "net.sf.py4j" % "py4j" % py4j,
    ),
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := (ThisBuild / name).value,
  )
  .aggregate(
    adapter,
    autobi,
  )
