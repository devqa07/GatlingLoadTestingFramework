## Load Testing Framework With Gatling, Scala and Gradle

This is a Load Testing Framework for REST APIs using Gatling and Scala. This framework is designed for restful-booker service/APIs,it uses gradle as build tool along with gradle-gatling plugin which allows executing single/multiple simulations from command line.

<h3>Pre-requisites:</h3>
- JDK 11 or higher<br>
- Gradle<br>
- Scala Plugin for IntelliJ IDEA

<h3>How To Run The Tests/Scripts:</h3>
- Clone the repository from GitHub to your local machine<br>
- To run any specific scenario against any environment <br/>
On Windows:<br>
  `.\gradlew clean -DEnvironment=test -Dprofile.json="[{"scenarioName":"getBookingDetails","injectionType":"open","injectionSteps":[{"stepName":"constantUsersPerSec","stepArgs":[1,10]" -Dlog.enabled="yes" gatlingRun-dev.perf.automation.simulations.bookerservice.BookerServiceSimulation`<br>
On Mac<br>
  `./gradlew clean -DEnvironment=test -Dprofile.json="[{"scenarioName":"getBookingDetails","injectionType":"open","injectionSteps":[{"stepName":"constantUsersPerSec","stepArgs":[1,10]" -Dlog.enabled="yes" gatlingRun-dev.perf.automation.simulations.bookerservice.BookerServiceSimulation`<br><br>
- We can also provide custom output directory path and execute the scenarios <br/>
  `./gradlew clean -DEnvironment=test -Dprofile.json="[{"scenarioName":"getBookingDetails","injectionType":"open","injectionSteps":[{"stepName":"constantUsersPerSec","stepArgs":[1,10]}]}]" -Dgatling.core.outputDirectoryBaseName="/outputDirectoryPath" gatlingRun-dev.perf.automation.simulations.bookerservice.BookerServiceSimulation`<br/><br/>
- We can configure the request/response logs to see the request and response after execution <br/>
  `./gradlew clean -DEnvironment=test -Dprofile.json="[{"scenarioName":"getBookingDetails","injectionType":"open","injectionSteps":[{"stepName":"constantUsersPerSec","stepArgs":[1,10]}]}]" -Dgatling.core.outputDirectoryBaseName="/outputDirectoryPath" -Dlog.enabled=yes gatlingRun-dev.perf.automation.simulations.bookerservice.BookerServiceSimulation`<br><br>
- We can also run the scenarios against wiremock then execute command without `Environment` property like below. This runs simulation(s) against `http://localhost:9999/` and uses mock responses placed under `src/main/resources` to return for the requests.  
  `.\gradlew clean -Dprofile.json="[{"scenarioName":"getBookingDetails","injectionType":"open","injectionSteps":[{"stepName":"constantUsersPerSec","stepArgs":[2,10]" -Dlog.enabled="yes" gatlingRun-dev.perf.automation.simulations.bookerservice.BookerServiceSimulation`<br>

<h3>Framework Structure:</h3>
<p><b>- src/test/resources/config:</b> It contains the environment and db details configurations</p>
<p><b>- src/test/scala/config:</b> It contains the configurations used throughout the framework</p>
<p><b>-src/test/scala/scenarios:</b> It contains the actual scenario scripts/flow for the APIs</p>
<p><b>-src/test/scala/simulations:</b> It contains the load test simulations for the services, we can just add the scenario/Api name in this class for new scenarios/APIs</p>
<p><b>-src/test/scala/utils:</b> It contains utility files that can be re-used throughout the framework. These classes include such as Load profiles, helper methods, DBUtils etc</p>
<p><b>-build.gradle:</b> The Gradle build script for the project. It includes the necessary dependencies and configurations for running Gatling load tests</p>
<p><b>-gatling.conf:</b> The Gatling configuration file that allows to customize various settings, such as the output directory, logging level and Gatling extensions</p>

<h3>Framework Components/Feature Details:</h3>
<p> - Environment specific configuration is added in resources/config/application.conf and retrieved using utility method.</p>
<p>- Creating HTTP configuration using singleton object that reduces duplicate objects</p>
<p>- Scenarios for APIs/service are created under `dev.perf.automation.scenarios.${service}.${api}`. Example: `dev.perf.automation.scenarios.bookerservice.GetBookingDetails` has scenarios for booker-service get bookings api.</p>
<p>- One simulation class is created under `dev.perf.automation.simulations.${service}` to dynamically run any number of scenarios.</p>
<p>- Environments added to `ALLOWED_ENV` variable from `Constants.scala` are allowed to use in this framework.</p>
<p>- Wiremock configured allowing users of this framework to test their simulations against mock environment before running them against actual environment.</p>
<p>- Gatling reports after the execution can be found under `build/reports` folder by default. Custom path can be provided from command line using `gatling.core.outputDirectoryBaseName` which will then allow all the report files to be created under given path.</p>
<p>- Logging of request/response is disabled by default and can be enabled with command line parameter `log.enabled`. `yes` will capture logs to log file under `build/logs/Request_Response*.log`. In case of custom path is provided for reports, then even logs would be pushed under same path.</p>
<p>- Dynamic input is enabled using json which will allow to run any simulation with different set users/time based on requirement without modifying the simulation written once. For this, we have to pass json as input for command line parameter `profile.json`</p>
<p>- This framework can be integrated with CI/CD tools like Jenkins and load tests/scripts can be executed from Jenkins</p>


For any questions please contact me on LinkedIn https://www.linkedin.com/in/devendra-singh-38b42311/
