# Case Submission Test Automation Project

This is the Case Submission test automation project. It supports testing Web UI through:
* Selenide as a wrapper on top of Selenium
* Desktop chrome browser
* Other desktop browsers will be included after clarification
* Allure for reporting

Additionally, test environment and data is going to be configured by Salesforce DX. 

## Table of Contents

1. Development requirements
2. Configuring test project
3. Creating tests
3. Running tests 
4. Allure report
5. Project structure
6. Maven plugins
7. Gitlab CI

## 1. Development requirements

**Required:** Chrome browser, Java Development Kit 11 (JDK11) and maven 3 should be present on environment in order to develop and/or run the tests.

Also, https://projectlombok.org/ is used to simplify creating/editing Java POJOs (data models). So in order to compile Project from IDE Lombok plugin should be installed.

## 2. Configuring test project

Run this command to ensure the code has no errors:

```
$ mvn clean install -DskipTests=true
```

## 3. Creating tests

Test classes must be inherited from  or the class which is its child. 
It contains a set of auxiliary methods which carry out useful work behind the scene such as downloading/starting/stopping the browser driver, collecting environment properties for reporting purpose, activating TestNG listeners and so on.

## 3. Running tests

To trigger the execution of the test cases you have to specify the next parameters:

* ***Suite:*** `-Dsuite={suite}` where `{suite}` is suite name without `.xml`
* ***Browser:*** `-Ddriver.type={driver.type}` where `{driver.type}` is value from `engine.DriverType.class`. 
In case of executing the test cases on the remote machine you have to specify `-Dremote.driver.type={remote.driver.type}` and `-Dremote.url={remote.url}` along with `-Ddriver.type=REMOTE` 
where `{remote.driver.type}` is a value from `engine.RemoteDriverType.class` and `{remote.url}` is the endpoint of selenium grid 
* ***Test Env:*** salesforce dx url which doesn't require an authentication `-Dsfdx.url={sfdx.url}` OR 
  salesforce (dx) url which requires an authentication `-Dsf.url={sf.url} -Dsf.user={sf.user} -Dsf.pass={sf.pass}`

```
$ mvn clean install -Dsuite={suite} -Ddriver.type={driver.type} -Dsfdx.url={sfdx.url}
```
or

```
$ mvn clean install -Dsuite={suite} -Ddriver.type={driver.type} -Dsf.url={sf.url} -Dsf.user={sf.user} -Dsf.pass={sf.pass}
```

Additional maven options: 
* ***Run Single Test:***: `-Dtest=pakage name of test class.class name -DfailIfNoTests=false` (ex., `mvn clean test -Djira.user={jira.user} -Djira.pass={jira.pass} -Dtest_plan_name={test_plan_name} -Dtest=api.assesment.SuccessTestCycleCreationTest -DfailIfNoTests=false`)
 
* ***Parallel Execution:*** `-Dparallel.mode={parallel.mode}` and `-Dthread.count={thread.count}` where `{parallel.mode}` is a value from `org.testng.xml.XmlSuite.ParallelMode.class` and `{thread.count}` is number of threads.
This values will override the one defined in suite(xml) file.   
 
## 4. Allure report

Once tests were executed `tests` module will contain `target\allure-results` directory. Content of that directory is used for Allure report generating.

In order to generate report after run use `mvn allure:report`. Once command is completed `allure-report` directory is created in project root. You can observe the report by opening `allure-report\index.html` in web browser.

Also, `mvn allure:serve` could be used for both report generating and opening it in your default web browser. Please note that in this case report is hosted through started jetty server.

## 5. Project structure

* `common` module should contain general Java code reused across other modules
* `tests` module should contain automated test cases
* `ui` module should contain methods for driving AUT (Application Under Test) through Web UI  
* `sdf-maven-plugin` module that contains additional maven goals which are described in            

## 6. Maven plugins

* ***Archive report:*** `mvn sfd:archive` archive allure-report to .zip format.
* ***Upload to S3:*** `mvn sfd:aws -Daws.access.key={aws.access.key} -Daws.secret.key={aws.secret.key} -Daws.bucket.name={aws.bucket.name} -Daws.region={aws.region}` where `{aws.access.key}` is your S3 access key
 and `{aws.secret.key}` is  your S3 secret key (they auto-generated when S3 bucket created), `{aws.bucket.name}`- your bucket name and `{aws.region}` bucket region. Upload zipped archive to S3 bucket and generate link to download it.
* ***Send Mail:*** `mvn sfd:mail -Demail.account={email.account} -Demail.password={email.password} -Demail.recipients={email.recipients}` where `{email.account}` and `{email.password}` email credentials from which letter will be send and `{email.recipients}` list of users emails. Used for sending download url of allure report via email.
* ***Test Plan Update:*** `mvn sfd:synapse -Djira.user={jira.user} -Djira.pass={jira.pass} -Dtest_plan_name={test_plan_name}` where `{jira.user}` and `{jira.pass}` it's jira account credentials and `{test_plan_name}` - test plan which we will update.
Test methods should be annotated by `io.qameta.allure.TmsLink.class` or `io.qameta.allure.TmsLinks.class` with the value of Jira test case ID for example `ELEM-<XXX>`.
    
## 7. Gitlab CI
Gitlab CI have 4 jobs:

1. execute_QA_tests_preconditions
2. execute_QA_tests
3. publish_QA_results_to_synapseRT
4. publish_QA_report

`execute_QA_tests_preconditions` is used for setting precondition such as:
* Create test date
* Create new SalesForce user
* Add fields

`execute_QA_tests` it's used for running test

`publish_qa_results_to_synapseRT` it's used `mvn sfd:synapse` command form maven plugins to create new Test plan, add Cycle to it, add all test cases , and updated statuses of this TC.

`publish_QA_report` it's used for publish allure report to gitlab pages.