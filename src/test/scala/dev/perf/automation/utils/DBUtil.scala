package dev.perf.automation.utils

import java.sql.ResultSet

object DBUtil {

  def getSingleResult(sqlQuery: String, columnValueToRead: String): String = {
    val connection = Datasource.connectionPool.getConnection
    var dataFromDB = ""
    try {
      // Ensure SQL Statements are Read-Only
      val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      // Execute Given SQL Query
      val results = statement.executeQuery(sqlQuery)
      while (results.next()) {
        dataFromDB = results.getString(columnValueToRead)
      }
    } finally {
      connection.close()
    }
    dataFromDB
  }

  @throws[InterruptedException]
  def execSQLWithRetry(sqlQuery: String, columnValueToRead: String, sleepMilli: Int): String = {
    val maxRetryTime = 3
    var time = 0
    var result:String = null
    do {
      time += 1
      try {
        result = getSingleResult(sqlQuery, columnValueToRead)
        if (null == result) {
          println("retry, time:" + time)
          Thread.sleep(sleepMilli)
          result = getSingleResult(sqlQuery, columnValueToRead)
        }
      } catch {
        case e: Exception => e.printStackTrace()
      }
    } while ( {null == result && time < maxRetryTime })
    result
  }

    def updateDBRecord(sqlQuery: String): Unit = {
      val connection = Datasource.connectionPool.getConnection
      try {
        val statement = connection.createStatement()
        // Execute Given update SQL Query
        val results = statement.executeUpdate(sqlQuery)
      } finally {
        connection.close()
      }
    }
  }