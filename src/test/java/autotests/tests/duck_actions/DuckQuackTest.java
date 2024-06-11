package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты для duck-action-controller.")
@Feature("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends DuckActionsClient {
  @Test (description = "Проверка действия утки - крякать с нечетным id.")
  @CitrusTest
  public void successfulQuackNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
    int soundCount = 2;
    int repetitionCount = 3;
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE)
            .id(701);
    runner.variable("duckId", duck.id());
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));

    String responseSound = getResponseSound(duck.sound(), soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
    int soundCount = 2;
    int repetitionCount = 3;
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE)
            .id(700);
    runner.variable("duckId", duck.id());
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));
    String responseSound = getResponseSound(duck.sound(), soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }
}