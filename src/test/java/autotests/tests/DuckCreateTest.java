package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends DuckActionsClient {
  @Test (description = "Проверка создания утки с material = rubber.")
  @CitrusTest
  public void successfulCreateMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    createDuck(runner, color, height, material, sound, wingsState);
    validateOkResponse(runner, "{\n"
            + "  \"color\": \"" + color + "\",\n"
            + "  \"height\": " + height + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + material + "\",\n"
            + "  \"sound\": \"" + sound + "\",\n"
            + "  \"wingsState\": \"" + wingsState + "\"\n"
            + "}");
  }

  @Test (description = "Проверка создания утки с material = wood.")
  @CitrusTest
  public void successfulCreateMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
    String color = "yellow";
    double height = 0.1;
    String material = "wood";
    String sound = "quack";
    String wingsState = "ACTIVE";
    createDuck(runner, color, height, material, sound, wingsState);
    validateOkResponse(runner, "{\n"
            + "  \"color\": \"" + color + "\",\n"
            + "  \"height\": " + height + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + material + "\",\n"
            + "  \"sound\": \"" + sound + "\",\n"
            + "  \"wingsState\": \"" + wingsState + "\"\n"
            + "}");
  }
}