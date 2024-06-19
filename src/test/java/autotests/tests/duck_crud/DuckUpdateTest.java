package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты для duck-controller (crud).")
@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends DuckActionsClient {
  @Test (description = "Проверка изменения у утки цвета и высоты.")
  @CitrusTest
  public void successfulUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE)
            .id(700);
    runner.variable("duckId", duck.id());
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    duck.color("red");
    duck.height(5);
    updateDuck(runner, duck.color(), String.valueOf(duck.height()), duck.material(), duck.sound());
    validateResponseUsingResources(runner, "duckUpdateTest/successfulUpdate.json", HttpStatus.OK);
    validateDuckInDatabase(runner, String.valueOf(duck.id()), duck.color(), String.valueOf(duck.height()),
            duck.material(), duck.sound(), String.valueOf(duck.wingsState()));

  }

  @Test (description = "Проверка изменения у утки цвета и звука.")
  @CitrusTest
  public void successfulUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE)
            .id(701);
    runner.variable("duckId", duck.id());
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    duck.color("green");
    duck.sound("QUACK");
    updateDuck(runner, duck.color(), String.valueOf(duck.height()), duck.material(), duck.sound());
    validateResponseUsingResources(runner, "duckUpdateTest/successfulUpdate.json", HttpStatus.OK);
    validateDuckInDatabase(runner, String.valueOf(duck.id()), duck.color(), String.valueOf(duck.height()),
            duck.material(), duck.sound(), String.valueOf(duck.wingsState()));
  }
}