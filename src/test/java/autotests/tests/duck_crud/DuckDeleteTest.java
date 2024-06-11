package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
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
    runner.variable("duckId", 700);
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    deleteDuck(runner);
    validateResponseUsingResources(runner, "duckDeleteTest/successfulDelete.json", HttpStatus.OK);
    validateDeletedDuckInDatabase(runner, "${duckId}");
  }
}