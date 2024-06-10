package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - лететь с активными крыльями.")
  @CitrusTest
  public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyActiveWings.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - лететь со связанными крыльями.")
  @CitrusTest
  public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.FIXED);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyFixedWings.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - лететь с неопределенным состоянием крыльев.")
  @CitrusTest
  public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.UNDEFINED);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyUndefinedWings.json", HttpStatus.OK);
  }
}