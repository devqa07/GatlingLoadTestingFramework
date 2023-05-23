package dev.perf.automation.utils

import io.gatling.core.Predef.Session
import scala.util.Random

object CommonUtil {

  def isNotBlank(input: String): Boolean = {
    if (input == null || input.trim.isEmpty)
      false
    else
      true
  }

  def getSystemProperty(key: String): String = {
    if (isNotBlank(key)) System.getProperty(key) else ""
  }

  def getSystemProperty(key: String, defaultValue: String): String = {
    val propertyValue = getSystemProperty(key)
    if (isNotBlank(propertyValue)) propertyValue else defaultValue
  }
  val rnd = new Random()
  def randomNumber(length: Int) = {
    rnd.alphanumeric.filter(_.isDigit).take(length).mkString
  }
  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def prepareMapWithNonEmptyValue(session:Session, queryParams:List[String]): Map[String, Any] ={
    var queryParamMap = Map.empty[String, Any]
    for(queryParam <- queryParams){
      if(!"".equals(session(queryParam).as[String]))
        queryParamMap = queryParamMap + (queryParam -> session(queryParam).as[String])
    }
    queryParamMap
  }
}