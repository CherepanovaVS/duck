package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - плыть с существующим id.")
  @CitrusTest
  public void successfulSwimExistId(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    swimDuck(runner, "${duckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimExistId.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - плыть с несуществующим id.")
  @CitrusTest
  public void successfulSwimNotExistId(@Optional @CitrusResource TestCaseRunner runner,
                                       @Optional @CitrusResource TestContext context) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.FIXED);
    createDuck(runner, duckProperties);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    context.setVariable("notExistDuckId", duckId + 100);

    swimDuck(runner, "${notExistDuckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimNotExistId.json", HttpStatus.OK);
  }
}