package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
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

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends DuckActionsClient {
  @Test (description = "Проверка характеристик утки с нечетным id, material = rubber.")
  @CitrusTest
  public void successfulGetPropertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    runner.variable("duckId", 701);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    getPropertiesDuck(runner);
    validateResponseUsingPayloads(runner, duck, HttpStatus.OK);
  }

  @Test (description = "Проверка характеристик утки с четным id, material = wood.")
  @CitrusTest
  public void successfulGetPropertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("wood")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    runner.variable("duckId", 700);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    getPropertiesDuck(runner);
    validateResponseUsingPayloads(runner, duck, HttpStatus.OK);
  }
}