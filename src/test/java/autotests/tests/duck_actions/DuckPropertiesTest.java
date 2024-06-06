package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends DuckActionsClient {
  @Test (description = "Проверка характеристик утки с нечетным id, material = rubber.")
  @CitrusTest
  public void successfulGetPropertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                               @Optional @CitrusResource TestContext context) {
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    if (duckId % 2 == 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    getPropertiesDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"color\": \"" + color + "\",\n"
            + "  \"height\": " + height + ",\n"
            + "  \"material\": \"" + material + "\",\n"
            + "  \"sound\": \"" + sound + "\",\n"
            + "  \"wingsState\": \"" + wingsState + "\"\n"
            + "}", "OK");
  }

  @Test (description = "Проверка характеристик утки с четным id, material = wood.")
  @CitrusTest
  public void successfulGetPropertiesEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                            @Optional @CitrusResource TestContext context) {
    String color = "yellow";
    double height = 0.1;
    String material = "wood";
    String sound = "quack";
    String wingsState = "ACTIVE";
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    if (duckId % 2 != 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    getPropertiesDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"color\": \"" + color + "\",\n"
            + "  \"height\": " + height + ",\n"
            + "  \"material\": \"" + material + "\",\n"
            + "  \"sound\": \"" + sound + "\",\n"
            + "  \"wingsState\": \"" + wingsState + "\"\n"
            + "}", "OK");
  }
}