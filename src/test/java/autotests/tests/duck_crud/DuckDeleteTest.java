package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTest extends DuckActionsClient {
  @Test (description = "Проверка удаления утки.")
  @CitrusTest
  public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    deleteDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"message\": \"Duck is deleted\"\n"
            + "}", "OK");
  }
}