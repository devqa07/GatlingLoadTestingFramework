package dev.perf.automation.utils

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.closed.ClosedInjectionStep
import io.gatling.core.controller.inject.open.OpenInjectionStep
import io.gatling.core.controller.throttle.ThrottleStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import org.json.{JSONArray, JSONObject}
import java.util
import scala.concurrent.duration._
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.language.postfixOps


object LoadProfiles {

  private def getOpenInjectionStep(stepName: String, stepArgs: JSONArray): Any = {
    stepName match {
      case "rampUsers" => rampUsers(stepArgs.getInt(0)) during (stepArgs.getInt(1) seconds)
      case "heavisideUsers" => heavisideUsers(stepArgs.getInt(0)) during (stepArgs.getInt(1) seconds)
      case "atOnceUsers" => atOnceUsers(stepArgs.getInt(0))
      case "constantUsersPerSec" => constantUsersPerSec(stepArgs.getDouble(0)) during (stepArgs.getInt(1) seconds)
      case "rampUsersPerSec" => rampUsersPerSec(stepArgs.getInt(0)) to (stepArgs.getInt(1)) during (stepArgs.getInt(2) seconds)
      case "nothingFor" => nothingFor(stepArgs.getInt(0) seconds)
      case _ => None
    }
  }

  private def getClosedInjectionStep(stepName: String, stepArgs: JSONArray): Any = {
    stepName match {
      case "constantConcurrentUsers" => constantConcurrentUsers(stepArgs.getInt(0)).during(stepArgs.getInt(1) seconds)
      case "rampConcurrentUsers" => rampConcurrentUsers(stepArgs.getInt(0)).to(stepArgs.getInt(1)).during(stepArgs.getInt(2) seconds)
      case _ => None
    }
  }

  private def getThrottleStep(stepName: String, stepArgs: JSONArray): Any = {
    stepName match {
      case "reachRps" => reachRps(stepArgs.getInt(0)) in (stepArgs.getInt(1) seconds)
      case "holdFor" => holdFor(stepArgs.getInt(0) seconds)
      case "jumpToRps" => jumpToRps(stepArgs.getInt(0))
      case _ => None
    }
  }

  private def prepareOpenInjectionProfile(injectionSteps: JSONArray): Iterable[OpenInjectionStep] = {
    val injectStepList = new util.ArrayList[OpenInjectionStep]()
    for (j <- 0 until injectionSteps.length()) {
      //For each injection step
      val injectionStep = injectionSteps.getJSONObject(j)
      val stepName = getStepName(injectionStep)
      val stepArgs = getStepArgs(injectionStep)
      val step = getOpenInjectionStep(stepName, stepArgs)
      if (None != step) {
        injectStepList.add(step.asInstanceOf[OpenInjectionStep])
      }
    }
    injectStepList.asScala
  }

  private def prepareClosedInjectionProfile(injectionSteps: JSONArray): Iterable[ClosedInjectionStep] = {
    val injectStepList = new util.ArrayList[ClosedInjectionStep](injectionSteps.length())
    for (j <- 0 until injectionSteps.length()) {
      //For each injection step
      val injectionStep = injectionSteps.getJSONObject(j)
      val stepName = getStepName(injectionStep)
      val stepArgs = getStepArgs(injectionStep)
      val step = getClosedInjectionStep(stepName, stepArgs)
      if (None != step) {
        injectStepList.add(step.asInstanceOf[ClosedInjectionStep])
      }
    }
    injectStepList.asScala
  }

  private def addThrottle(injectionSteps: JSONArray): Iterable[ThrottleStep] = {
    val injectStepList = new util.ArrayList[ThrottleStep]()
    for (j <- 0 until injectionSteps.length()) {
      //For each injection step
      val injectionStep = injectionSteps.getJSONObject(j)
      val stepName = getStepName(injectionStep)
      val stepArgs = getStepArgs(injectionStep)
      val step = getThrottleStep(stepName, stepArgs)
      if (None != step) {
        injectStepList.add(step.asInstanceOf[ThrottleStep])
      }
    }
    injectStepList.asScala
  }

  private def getStepName(step: JSONObject) = {
    step.getString("stepName")
  }

  private def getStepArgs(step: JSONObject) = {
    step.getJSONArray("stepArgs")
  }


  def create(json: String, scenarios: Map[String, ScenarioBuilder]): Iterable[PopulationBuilder] = {
    val inputJson = new JSONArray(json)
    val scnList = new util.ArrayList[PopulationBuilder]()
    for (i <- 0 until inputJson.length()) {
      val scenario = inputJson.getJSONObject(i)
      val injectionType: String = scenario.getString("injectionType")
      val scenarioName: String = scenario.getString("scenarioName")
      val isThrottleEnabled: Boolean = scenario.optBoolean("enableThrottle")
      val injectionSteps = scenario.getJSONArray("injectionSteps")

      var populationBuilder = None: Option[PopulationBuilder]
      if ("open".equalsIgnoreCase(injectionType)) {
        populationBuilder = Some(scenarios(scenarioName).inject(this.prepareOpenInjectionProfile(injectionSteps)))
      } else if ("closed".equalsIgnoreCase(injectionType)) {
        populationBuilder = Some(scenarios(scenarioName).inject(this.prepareClosedInjectionProfile(injectionSteps)))
      }

      if (isThrottleEnabled) {
        scnList.add(populationBuilder.get.throttle(addThrottle(scenario.getJSONArray("throttleSteps"))))
      } else {
        scnList.add(populationBuilder.get)
      }
    }
    scnList.asScala
  }
}