package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
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
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    deleteDuck(runner);
    validateResponseUsingResources(runner, "duckDeleteTest/successfulDelete.json", HttpStatus.OK);
  }
}