package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - лететь с активными крыльями.")
  @CitrusTest
  public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("duckId", 703);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyActiveWings.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - лететь со связанными крыльями.")
  @CitrusTest
  public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("duckId", 704);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "FIXED");
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyFixedWings.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - лететь с неопределенным состоянием крыльев.")
  @CitrusTest
  public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("duckId", 705);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "UNDEFINED");
    flyDuck(runner);
    validateResponseUsingResources(runner, "duckFlyTest/successfulFlyUndefinedWings.json", HttpStatus.OK);
  }
}