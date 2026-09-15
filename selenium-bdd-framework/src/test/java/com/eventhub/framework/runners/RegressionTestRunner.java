package com.eventhub.framework.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;


 * Executes every @Regression-tagged scenario across all modules for
 * complete end-to-end coverage. Run via:
 *   mvn test -Dtest=RegressionTestRunner
 *   mvn test -Pregression

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@Regression")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.eventhub.framework.hooks,com.eventhub.framework.stepdefinitions")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, "
        + "html:reports/cucumber-html-report.html, "
        + "json:reports/cucumber-report.json, "
        + "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, "
        + "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")
public class RegressionTestRunner {
}
