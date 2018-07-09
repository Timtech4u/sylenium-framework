package io.github.symonk.listeners;

import com.codeborne.selenide.Configuration;
import io.github.symonk.common.helpers.reporting.ReportHelper;
import io.github.symonk.common.helpers.reporting.ReportInteractable;
import io.github.symonk.configurations.properties.AutomationProperties;
import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;

@Slf4j
public class TestExecutionListener implements IExecutionListener {

  private final AutomationProperties automationProperties = new AutomationProperties();
  private final ReportInteractable reportHelper = new ReportHelper(automationProperties);

  @Override
  public void onExecutionStart() {
    log.info("test run starting!");
    configureTestRun();
    pushReportInformation();
  }

  @Override
  public void onExecutionFinish() {
    log.info("test run finished!");
  }

  private void configureTestRun() {
    if (automationProperties.shouldRunDistributed()) {
      Configuration.remote = automationProperties.getGridEndpoint();
    }
    if (automationProperties.browserUseCustomDimensions()) {
      Configuration.startMaximized = false;
      Configuration.browserSize = automationProperties.getBrowserDimensions();
    }

    Configuration.baseUrl = automationProperties.getBaseUrl();
    Configuration.timeout = automationProperties.getWaitTimeout();
  }

  private void pushReportInformation() {
    this.reportHelper.pushDynamicTestRunPropertiesToReport();
  }
}
