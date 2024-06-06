package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - лететь с активными крыльями.")
  @CitrusTest
  public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    flyDuck(runner);
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"I'm flying\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - лететь со связанными крыльями.")
  @CitrusTest
  public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "FIXED");
    getDuckId(runner);
    flyDuck(runner);
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"I can't fly\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - лететь с неопределенным состоянием крыльев.")
  @CitrusTest
  public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "UNDEFINED");
    getDuckId(runner);
    flyDuck(runner);
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"Wings are not detected :(\"\n"
            + "}");
  }
}