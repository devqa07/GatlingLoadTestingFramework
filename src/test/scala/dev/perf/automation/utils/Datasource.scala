package dev.perf.automation.utils

import dev.perf.automation.config.{Config, Constants}
import org.apache.commons.dbcp2.BasicDataSource

object Datasource {
  val connectionPool = new BasicDataSource()
  connectionPool.setUsername(AWSUtil.getSecret(Config.read().getString(Constants.DB_USERNAME_SECRET_NAME), Config.read().getString(Constants.DB_USERNAME_SECRET_KEY).replace(Constants.ENV,Config.execEnv())))
  connectionPool.setPassword(AWSUtil.getSecret(Config.read().getString(Constants.DB_PASSWORD_SECRET_NAME), Config.read().getString(Constants.DB_PASSWORD_SECRET_KEY).replace(Constants.ENV,Config.execEnv())))
  connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver")
  connectionPool.setUrl("jdbc:mysql://"+Config.read().getString("dbHost")+":"+Config.read().getString("dbPort")+"/")
  connectionPool.setInitialSize(5)
  connectionPool.setMaxTotal(10)
  connectionPool.setMinIdle(2)
}
