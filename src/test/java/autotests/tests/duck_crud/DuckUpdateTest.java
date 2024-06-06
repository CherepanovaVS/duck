package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends DuckActionsClient {
  @Test (description = "Проверка изменения у утки цвета и высоты.")
  @CitrusTest
  public void successfulUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    updateDuck(runner, "red", 5, "rubber", "quack");
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
            + "}");
  }

  @Test (description = "Проверка изменения у утки цвета и звука.")
  @CitrusTest
  public void successfulUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    updateDuck(runner, "green", 0.1, "rubber", "QUACK");
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
            + "}");
  }
}