package autotests.tests.duck_crud;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты для duck-controller (crud).")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckActionsClient {

  Duck duck1 = new Duck().color("yellow").height(0.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
  Duck duck2 = new Duck().color("yellow").height(5).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
  Duck duck3 = new Duck().color("yellow").height(10).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
  Duck duck4 = new Duck().color("yellow").height(0.1).material("rubber").sound("QUACK").wingsState(WingsState.ACTIVE);
  Duck duck5 = new Duck().color("yellow").height(0.1).material("rubber").sound("quack").wingsState(WingsState.FIXED);

  @DataProvider(name = "duckList1")
  public Object[][] DuckProvider1() {
    return new Object[][]{
            {"yellow", 0.1, "rubber", "quack", "ACTIVE",
                    "{\n  \"color\": \"yellow\",\n  \"height\": 0.1,\n  \"id\": \"@isNumber()@\",\n  \"material\": \"rubber\",\n"
                    + "  \"sound\": \"quack\",\n  \"wingsState\": \"ACTIVE\"\n}", HttpStatus.OK, null},
            {"yellow", 5, "rubber", "quack", "ACTIVE",
                    "{\n  \"color\": \"yellow\",\n  \"height\": 5.0,\n  \"id\": \"@isNumber()@\",\n  \"material\": \"rubber\",\n"
                    + "  \"sound\": \"quack\",\n  \"wingsState\": \"ACTIVE\"\n}", HttpStatus.OK, null},
            {"yellow", 10, "rubber", "quack", "ACTIVE",
                    "{\n  \"color\": \"yellow\",\n  \"height\": 10.0,\n  \"id\": \"@isNumber()@\",\n  \"material\": \"rubber\",\n"
                    + "  \"sound\": \"quack\",\n  \"wingsState\": \"ACTIVE\"\n}", HttpStatus.OK, null},
            {"yellow", 0.1, "rubber", "QUACK", "ACTIVE",
                    "{\n  \"color\": \"yellow\",\n  \"height\": 0.1,\n  \"id\": \"@isNumber()@\",\n  \"material\": \"rubber\",\n"
                    + "  \"sound\": \"QUACK\",\n  \"wingsState\": \"ACTIVE\"\n}", HttpStatus.OK, null},
            {"yellow", 0.1, "rubber", "quack", "FIXED",
                    "{\n  \"color\": \"yellow\",\n  \"height\": 0.1,\n  \"id\": \"@isNumber()@\",\n  \"material\": \"rubber\",\n"
                    + "  \"sound\": \"quack\",\n  \"wingsState\": \"FIXED\"\n}", HttpStatus.OK, null},
    };
  }

  @DataProvider(name = "duckList")
  public Object[][] DuckProvider() {
    return new Object[][]{
            {duck1, "{\n"
                    + "  \"color\": \"" + duck1.color() + "\",\n"
                    + "  \"height\": " + duck1.height() + ",\n"
                    + "  \"id\": \"@isNumber()@\",\n"
                    + "  \"material\": \"" + duck1.material() + "\",\n"
                    + "  \"sound\": \"" + duck1.sound() + "\",\n"
                    + "  \"wingsState\": \"" + duck1.wingsState() + "\"\n"
                    + "}", HttpStatus.OK, null},
            {duck2, "{\n"
                    + "  \"color\": \"" + duck2.color() + "\",\n"
                    + "  \"height\": " + duck2.height() + ",\n"
                    + "  \"id\": \"@isNumber()@\",\n"
                    + "  \"material\": \"" + duck2.material() + "\",\n"
                    + "  \"sound\": \"" + duck2.sound() + "\",\n"
                    + "  \"wingsState\": \"" + duck2.wingsState() + "\"\n"
                    + "}", HttpStatus.OK, null},
            {duck3, "{\n"
                    + "  \"color\": \"" + duck3.color() + "\",\n"
                    + "  \"height\": " + duck3.height() + ",\n"
                    + "  \"id\": \"@isNumber()@\",\n"
                    + "  \"material\": \"" + duck3.material() + "\",\n"
                    + "  \"sound\": \"" + duck3.sound() + "\",\n"
                    + "  \"wingsState\": \"" + duck3.wingsState() + "\"\n"
                    + "}", HttpStatus.OK, null},
            {duck4, "{\n"
                    + "  \"color\": \"" + duck4.color() + "\",\n"
                    + "  \"height\": " + duck4.height() + ",\n"
                    + "  \"id\": \"@isNumber()@\",\n"
                    + "  \"material\": \"" + duck4.material() + "\",\n"
                    + "  \"sound\": \"" + duck4.sound() + "\",\n"
                    + "  \"wingsState\": \"" + duck4.wingsState() + "\"\n"
                    + "}", HttpStatus.OK, null},
            {duck5, "{\n"
                    + "  \"color\": \"" + duck5.color() + "\",\n"
                    + "  \"height\": " + duck5.height() + ",\n"
                    + "  \"id\": \"@isNumber()@\",\n"
                    + "  \"material\": \"" + duck5.material() + "\",\n"
                    + "  \"sound\": \"" + duck5.sound() + "\",\n"
                    + "  \"wingsState\": \"" + duck5.wingsState() + "\"\n"
                    + "}", HttpStatus.OK, null},
    };
  }

  @Test(description = "Проверка создания утки.", dataProvider = "duckList")
  @CitrusTest
  @CitrusParameters({"payload", "response", "status", "runner"})
  public void successfulCreate(Object payload, String response, HttpStatus status,
                               @Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, payload);
    validateResponseUsingStringAndExtractVariable(runner, response, status);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
  }

  @Test(description = "Проверка создания утки.", dataProvider = "duckList1")
  @CitrusTest
  @CitrusParameters({"color", "height", "material", "sound", "wingsState", "response", "status", "runner"})
  public void successfulCreate1(String color, double height, String material, String sound, String wingsState,
                                String response, HttpStatus status, @Optional @CitrusResource TestCaseRunner runner) {
    Duck duck = new Duck()
            .color(color)
            .height(height)
            .material(material)
            .sound(sound)
            .wingsState(WingsState.valueOf(wingsState));
    createDuck(runner, duck);
    validateResponseUsingStringAndExtractVariable(runner, response, status);
    runner.$(doFinally().actions(context->deleteDuckInDatabase(runner)));
  }

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
    validateResponseUsingStringAndExtractVariable(runner, "{\n"
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
    validateResponseUsingStringAndExtractVariable(runner, "{\n"
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