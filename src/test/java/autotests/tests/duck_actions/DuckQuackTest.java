package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - крякать с нечетным id.")
  @CitrusTest
  public void successfulQuackNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                       @Optional @CitrusResource TestContext context) {
    int soundCount = 2;
    int repetitionCount = 3;
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    // Если id четный, создадим ещё одну утку, чтобы у неё был нечетный id.
    if (duckId % 2 == 0) {
      createDuck(runner, duckProperties);
      getDuckId(runner);
    }

    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));

    String responseSound = getResponseSound(duckProperties.sound(), soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                    @Optional @CitrusResource TestContext context) {
    int soundCount = 2;
    int repetitionCount = 3;
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duckProperties);
    getDuckId(runner);

    int duckId = Integer.parseInt(context.getVariable("duckId"));
    // Если id нечетный, создадим ещё одну утку, чтобы у неё был четный id.
    if (duckId % 2 != 0) {
      createDuck(runner, duckProperties);
      getDuckId(runner);
    }

    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));

    String responseSound = getResponseSound(duckProperties.sound(), soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }
}