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
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - плыть с существующим id.")
  @CitrusTest
  public void successfulSwimExistId(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("duckId", 710);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    swimDuck(runner, "${duckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimExistId.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - плыть с несуществующим id.")
  @CitrusTest
  public void successfulSwimNotExistId(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("duckId", 711);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    runner.variable("notExistDuckId", 800);
    swimDuck(runner, "${notExistDuckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimNotExistId.json", HttpStatus.OK);
  }
}