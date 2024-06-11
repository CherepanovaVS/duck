package autotests.tests.duck_actions;

import autotests.clients.DuckActionsClient;
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
    runner.variable("duckId", 701);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));
    String responseSound = getResponseSound("quack", soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
    int soundCount = 2;
    int repetitionCount = 3;
    runner.variable("duckId", 700);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    createDuckInDatabase(runner, "yellow", "0.1", "rubber", "quack", "ACTIVE");
    quackDuck(runner, String.valueOf(soundCount), String.valueOf(repetitionCount));
    String responseSound = getResponseSound("quack", soundCount, repetitionCount);
    validateResponseUsingString(runner, "{\n  \"sound\": \"" + responseSound + "\"\n}", HttpStatus.OK);
  }
}