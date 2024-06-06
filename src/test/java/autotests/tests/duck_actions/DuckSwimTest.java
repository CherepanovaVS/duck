package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckSwimTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - плыть с существующим id.")
  @CitrusTest
  public void successfulSwimExistId(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    swimDuck(runner, "${duckId}");
    validateOkResponse(runner, "{\n"
            + "  \"message\": \"I'm swimming\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - плыть с несуществующим id.")
  @CitrusTest
  public void successfulSwimNotExistId(@Optional @CitrusResource TestCaseRunner runner,
                                       @Optional @CitrusResource TestContext context) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "FIXED");
    getDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    context.setVariable("notExistDuckId", duckId + 100);

    swimDuck(runner, "${notExistDuckId}");
    validateNotFoundResponse(runner, "{\n"
            + "  \"message\": \"Duck with id = ${notExistDuckId} isn't found\"\n"
            + "}");
  }
}