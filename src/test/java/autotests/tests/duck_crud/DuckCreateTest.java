package autotests.tests.duck_crud;

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

@Epic("Тесты для duck-controller (crud).")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckActionsClient {
  @Test (description = "Проверка создания утки с material = rubber.")
  @CitrusTest
  public void successfulCreateMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("rubber")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duck);
    validateResponseUsingString(runner, "{\n"
            + "  \"color\": \"" + duck.color() + "\",\n"
            + "  \"height\": " + duck.height() + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + duck.material() + "\",\n"
            + "  \"sound\": \"" + duck.sound() + "\",\n"
            + "  \"wingsState\": \"" + duck.wingsState() + "\"\n"
            + "}", HttpStatus.OK);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    validateDuckInDatabase(runner, "${duckId}", duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
  }

  @Test (description = "Проверка создания утки с material = wood.")
  @CitrusTest
  public void successfulCreateMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color("yellow")
            .height(0.1)
            .material("wood")
            .sound("quack")
            .wingsState(WingsState.ACTIVE);
    createDuck(runner, duck);
    validateResponseUsingString(runner, "{\n"
            + "  \"color\": \"" + duck.color() + "\",\n"
            + "  \"height\": " + duck.height() + ",\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"" + duck.material() + "\",\n"
            + "  \"sound\": \"" + duck.sound() + "\",\n"
            + "  \"wingsState\": \"" + duck.wingsState() + "\"\n"
            + "}", HttpStatus.OK);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
    validateDuckInDatabase(runner, "${duckId}", duck.color(), String.valueOf(duck.height()), duck.material(),
            duck.sound(), String.valueOf(duck.wingsState()));
  }
}