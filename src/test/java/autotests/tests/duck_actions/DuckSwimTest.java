package autotests.tests.duck_actions;

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

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - плыть с существующим id.")
  @CitrusTest
  public void successfulSwimExistId(@Optional @CitrusResource TestCaseRunner runner) {
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
    swimDuck(runner, "${duckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimExistId.json", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - плыть с несуществующим id.")
  @CitrusTest
  public void successfulSwimNotExistId(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.FIXED)
            .id(700);
    runner.variable("duckId", duck.id());
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    runner.variable("notExistDuckId", duck.id() + 100);
    swimDuck(runner, "${notExistDuckId}");
    validateResponseUsingResources(runner, "duckSwimTest/successfulSwimNotExistId.json", HttpStatus.OK);
  }
}