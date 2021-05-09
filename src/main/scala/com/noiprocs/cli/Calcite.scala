package com.noiprocs.cli

import scala.io.Source

object Calcite {
  def appBannerVars: Map[String, String] = Map(
    "appVersion" -> getClass.getPackage.getImplementationVersion,
//    "gitCommit" -> versionit.gitCommit,
    "scalaVersionNumberString" -> scala.util.Properties.versionNumberString,
    "scalaVersionString" -> scala.util.Properties.versionString,
    "scalaVersionMsg" -> scala.util.Properties.versionMsg
  )

  // Generate banner string
  def appBanner: String = {
    val inputStream = getClass.getResourceAsStream("/banner.txt")
    val rawBanner = Source.fromInputStream(inputStream).mkString

    inputStream.close()

    appBannerVars.foldLeft(rawBanner) {
      case (banner, (name, value)) =>
        if (value != null) banner.replace('$' + name, value) else banner
    }
  }

}
