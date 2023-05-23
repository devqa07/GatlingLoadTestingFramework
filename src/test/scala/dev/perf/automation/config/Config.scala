package dev.perf.automation.config

import com.typesafe.config._
import dev.perf.automation.config.Constants.URL
import dev.perf.automation.utils.CommonUtil
import io.gatling.core.Predef._
import io.gatling.http.HeaderValues.ApplicationJson
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

import java.io.File

object Config {

  var configProp:Config = null
  var env:String = ""
  var protocol:HttpProtocolBuilder = null
  var executionEnv: String = ""

  private val HttpHeaders = http
    .header(name = "Accept", value = ApplicationJson)
    .header(name = "Content-Type", value = ApplicationJson)

  def getProtocol(): HttpProtocolBuilder = {
    if(null == protocol){
      protocol = HttpHeaders.baseUrl(read().getString(URL))
        .disableWarmUp//disable automatic call to https://gatling.io on every first request
    }
    protocol
  }

  def read():Config = {
    if( !CommonUtil.isNotBlank(env) ){
      env = CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL)
    }
    if(null == configProp){
      configProp = ConfigFactory.parseFile(new File("src/test/resources/config/application.conf"))
    }
    configProp.getConfig(env).withFallback(configProp)
  }

  def execEnv(): String = {
    if (!CommonUtil.isNotBlank(executionEnv)) {
      val envArray = CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL).split("-")
      executionEnv = if (envArray.length>1) envArray(1) else envArray(0)
    }
    executionEnv
  }
}