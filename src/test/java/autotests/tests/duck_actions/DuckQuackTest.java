package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckQuackTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - крякать с нечетным id.")
  @CitrusTest
  public void successfulQuackNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                       @Optional @CitrusResource TestContext context) {
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    int soundCount = 2;
    int repetitionCount = 3;
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    // Если id четный, создадим ещё одну утку, чтобы у неё был нечетный id.
    if (duckId % 2 == 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    quackDuck(runner, soundCount, repetitionCount);

    String responseSound = getResponseSound(sound, soundCount, repetitionCount);
    validateOkResponse(runner, "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                    @Optional @CitrusResource TestContext context) {
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    int soundCount = 2;
    int repetitionCount = 3;
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    // Если id нечетный, создадим ещё одну утку, чтобы у неё был четный id.
    if (duckId % 2 != 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    quackDuck(runner, soundCount, repetitionCount);

    String responseSound = getResponseSound(sound, soundCount, repetitionCount);
    validateOkResponse(runner, "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
            + "}");
  }
}