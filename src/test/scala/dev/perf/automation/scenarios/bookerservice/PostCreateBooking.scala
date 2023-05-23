package dev.perf.automation.scenarios.bookerservice

import dev.perf.automation.config.Constants
import dev.perf.automation.utils.CommonUtil
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object PostCreateBooking {

  val input = csv(fileName = "data/bookerservice/" + CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL) + "/CreateBooking.csv").circular

  def createBooking(): ChainBuilder = {
    feed(input)
      .exec(http(requestName = "API to create Booking")
        .post(url = "/booking")
        .body(ElFileBody("data/bookerservice/CreateBooking_RequestBody_Template.json")).asJson
        .check(status.is(200)))//Check for status code in response
  }

  //setUp
  val scn = scenario(scenarioName = "scenario to create Booking")
    .exec(createBooking())
}
