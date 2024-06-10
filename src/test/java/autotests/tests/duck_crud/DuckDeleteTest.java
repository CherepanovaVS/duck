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

@Epic("Тесты для duck-controller (crud).")
@Feature("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends DuckActionsClient {
  @Test (description = "Проверка удаления утки.")
  @CitrusTest
  public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE)
            .id(700);
    runner.variable("duckId", duck.id());
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    deleteDuck(runner);
    validateResponseUsingResources(runner, "duckDeleteTest/successfulDelete.json", HttpStatus.OK);
    validateDeletedDuckInDatabase(runner, String.valueOf(duck.id()));
  }
}