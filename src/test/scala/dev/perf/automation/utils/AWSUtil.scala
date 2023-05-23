package dev.perf.automation.utils

import com.fasterxml.jackson.core.JsonParseException
import dev.perf.automation.config.Constants
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats, MappingException}
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.{GetSecretValueRequest, ResourceNotFoundException, SecretsManagerException}

import java.net.URI

object AWSUtil {
  var awsSecretsManagerClient: SecretsManagerClient = null
  var s3Client: S3Client = null

  def getSecret(secretId:String, secretKey: String): String = {
    var secretString: String = null
    try {
      if(null == awsSecretsManagerClient){
        if (Constants.LOCAL.equals(CommonUtil.getSystemProperty(Constants.ENVIRONMENT, Constants.LOCAL))) {
          awsSecretsManagerClient = SecretsManagerClient.builder()
            .overrideConfiguration(ClientOverrideConfiguration.builder().defaultProfileName("localstack").build())
            .endpointOverride(new URI("http://localhost:9999"))
            .build()
        } else {
          awsSecretsManagerClient = SecretsManagerClient.builder()
            .build()
        }
      }
      val secret = awsSecretsManagerClient.getSecretValue(GetSecretValueRequest.builder()
        .secretId(secretId)
        .build())
      if (null != secret) {
        if(!"".equals(secretKey)){
          val a = parseJson(secret.secretString())
          implicit val formats: Formats = DefaultFormats
          secretString = (a \secretKey).extract[String]
        }else{
          secretString = secret.secretString()
        }
      }
    } catch {
      case e: ResourceNotFoundException => println("Couldn't find that secret in Secrets Manager:\n" + e)
        System.exit(1)
      case e: SecretsManagerException => println("Exception occurred while fetching secrets from Secrets Manager:\n" + e)
        System.exit(1)
      case e: JsonParseException => println("Exception occurred while parsing secrets json from AWS. Revalidate the data")
        System.exit(1)
      case e: MappingException => println("Requested secretKey:'"+secretKey+"' is not available from AWS SecretsManger")
        System.exit(1)
      case e: Exception => println("Unhandled Exception:\n" + e)
        System.exit(1)
    }
    secretString
  }
}