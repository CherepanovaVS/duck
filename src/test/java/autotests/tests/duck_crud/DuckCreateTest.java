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

public class DuckCreateTest extends DuckActionsClient {
  @Test (description = "Проверка создания утки с material = rubber.")
  @CitrusTest
  public void successfulCreateMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    validateResponseUsingString(runner, "{\n"
            + "  \"color\": \"" + duckProperties.color() + "\",\n"
            + "  \"height\": " + duckProperties.height() + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + duckProperties.material() + "\",\n"
            + "  \"sound\": \"" + duckProperties.sound() + "\",\n"
            + "  \"wingsState\": \"" + duckProperties.wingsState() + "\"\n"
            + "}", HttpStatus.OK);
  }

  @Test (description = "Проверка создания утки с material = wood.")
  @CitrusTest
  public void successfulCreateMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("wood")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    validateResponseUsingString(runner, "{\n"
            + "  \"color\": \"" + duckProperties.color() + "\",\n"
            + "  \"height\": " + duckProperties.height() + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + duckProperties.material() + "\",\n"
            + "  \"sound\": \"" + duckProperties.sound() + "\",\n"
            + "  \"wingsState\": \"" + duckProperties.wingsState() + "\"\n"
            + "}", HttpStatus.OK);
  }
}