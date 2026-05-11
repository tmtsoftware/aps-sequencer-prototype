import sbt._
import java.io.FileReader
import java.util.Properties
import scala.util.Using

object Libs {
  private val Org     = "com.github.tmtsoftware.esw"
  private val Version = BuildProperties.read("esw.version")

  val `esw-ocs-dsl-kt` = Org %  "esw-ocs-dsl-kt" % Version
  val `esw-ocs-app`    = Org %% "esw-ocs-app"     % Version
}

object BuildProperties {
  def read(key: String): String =
    Using.resource(new FileReader("project/build.properties")) { reader =>
      val prop = new Properties()
      prop.load(reader)
      prop.getProperty(key)
    }
}
