package dev.perf.automation.config

object Constants {
  val ENVIRONMENT = "Environment"
  val LOCAL = "local"
  val URL = "url"
  val ALLOWED_ENV : List[String] = List("local", "test", "qa", "preprod")
  val ENV = "{env}"
  val DB_USERNAME_SECRET_NAME = "db_username_secret_name"
  val DB_PASSWORD_SECRET_NAME = "db_password_secret_name"
  val DB_USERNAME_SECRET_KEY = "db_username_secret_key"
  val DB_PASSWORD_SECRET_KEY = "db_password_secret_key"
}

