package dev.perf.automation.simulations.bookerservice

import dev.perf.automation.config.Config
import dev.perf.automation.scenarios.bookerservice._
import dev.perf.automation.simulations.BaseSimulation
import dev.perf.automation.utils.LoadProfiles

class BookerServiceSimulation extends BaseSimulation {

  val scenarios = Map (
    "getBookingDetails" -> GetBookingDetails.scn,
    "createBooking" -> PostCreateBooking.scn
  )

  setUp(LoadProfiles.create(json, scenarios).toList).protocols(Config.getProtocol())
}