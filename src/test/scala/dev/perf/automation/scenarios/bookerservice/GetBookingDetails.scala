package dev.perf.automation.scenarios.bookerservice

import dev.perf.automation.config.Constants
import dev.perf.automation.utils.CommonUtil
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object GetBookingDetails {

  val input = csv(fileName = "data/bookerservice/" + CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL) + "/GetBookingDetails.csv").circular

  def getBookingDetails(): ChainBuilder = {
    feed(input)
      .exec(http(requestName = "API To Get Booking details with bookingId")
        .get(url = "/booking/${bookingId}")
        .check(status.is(200))) //Check for status code in response
  }

  //setUp
  val scn = scenario(scenarioName = "Scenario To Get Booking details with bookingId")
    .exec(getBookingDetails())
}