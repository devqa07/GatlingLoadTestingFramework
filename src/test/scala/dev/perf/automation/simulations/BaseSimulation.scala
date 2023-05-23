package dev.perf.automation.simulations

import dev.perf.automation.config.{Constants, WiremockConfig}
import dev.perf.automation.utils.CommonUtil
import io.gatling.core.Predef._

class BaseSimulation extends Simulation{
  before {
    val environment = CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL)
    if( Constants.LOCAL == environment ){
      println("Starting wiremock server...")
      val wiremockConfig = WiremockConfig.init()
      while (!wiremockConfig.isServerRunning){
        Thread.sleep(5)
      }
    }else{
      println("Proceeding execution of simulation without wiremock....")
    }
  }

  val json = CommonUtil.getSystemProperty("profile.json")
  if(null==json || json.isEmpty){
    println("###########################################")
    println("# Provide valid simulation profile to run #")
    println("###########################################")
    System.exit(1)
  }

  after {
    WiremockConfig.shutdown()
    println("Simulation is finished!")
  }
}
