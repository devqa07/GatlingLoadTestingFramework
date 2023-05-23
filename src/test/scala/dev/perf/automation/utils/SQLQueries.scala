package dev.perf.automation.utils

import dev.perf.automation.config.Config

object SQLQueries {
  val deleteBookDetails = "DELETE from "+Config.read().getString("schemaPrefix")+"book_details where booking_id='${bookingId}'"
  val updateBookStatus =  "UPDATE "+Config.read().getString("schemaPrefix")+"book_details SET status='CANCELLED' where booking_id='${bookingId}'"
}