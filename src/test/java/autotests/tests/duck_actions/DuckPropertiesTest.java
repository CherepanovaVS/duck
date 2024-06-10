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
@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends DuckActionsClient {
  @Test (description = "Проверка характеристик утки с нечетным id, material = rubber.")
  @CitrusTest
  public void successfulGetPropertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                               @Optional @CitrusResource TestContext context) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    if (duckId % 2 == 0) {
      createDuck(runner, duckProperties);
      getDuckId(runner);
    }

    getPropertiesDuck(runner);
    validateResponseUsingPayloads(runner, duckProperties, HttpStatus.OK);
  }

  @Test (description = "Проверка характеристик утки с четным id, material = wood.")
  @CitrusTest
  public void successfulGetPropertiesEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                            @Optional @CitrusResource TestContext context) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("wood")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    if (duckId % 2 != 0) {
      createDuck(runner, duckProperties);
      getDuckId(runner);
    }

    getPropertiesDuck(runner);
    validateResponseUsingPayloads(runner, duckProperties, HttpStatus.OK);
  }
}