package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends DuckActionsClient {
  @Test (description = "Проверка изменения у утки цвета и высоты.")
  @CitrusTest
  public void successfulUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    updateDuck(runner, "red", "5", duckProperties.material(), duckProperties.sound());
    validateResponseUsingResources(runner, "duckUpdateTest/successfulUpdate.json", HttpStatus.OK);

  }

  @Test (description = "Проверка изменения у утки цвета и звука.")
  @CitrusTest
  public void successfulUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);
    updateDuck(runner, "green", String.valueOf(duckProperties.height()), duckProperties.material(), "QUACK");
    validateResponseUsingResources(runner, "duckUpdateTest/successfulUpdate.json", HttpStatus.OK);
  }
}