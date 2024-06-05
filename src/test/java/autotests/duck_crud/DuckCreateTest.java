package autotests.duck_crud;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreateTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка создания утки с material = rubber.")
  @CitrusTest
  public void successfulCreateMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);

    validateResponse(runner, "{\n"
            + "  \"color\": \"${color}\",\n"
            + "  \"height\": ${height},\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"${material}\",\n"
            + "  \"sound\": \"${sound}\",\n"
            + "  \"wingsState\": \"${wingsState}\"\n"
            + "}");
  }

  @Test (description = "Проверка создания утки с material = wood.")
  @CitrusTest
  public void successfulCreateMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "wood");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);

    validateResponse(runner, "{\n"
            + "  \"color\": \"${color}\",\n"
            + "  \"height\": ${height},\n"
            + "  \"id\": \"@isNumber()@\",\n"
            + "  \"material\": \"${material}\",\n"
            + "  \"sound\": \"${sound}\",\n"
            + "  \"wingsState\": \"${wingsState}\"\n"
            + "}");
  }

  /**
   * Создание утки.
   * @param runner
   */
  public void createDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .post("/api/duck/create")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\n"
                    + "  \"color\": \"${color}\",\n"
                    + "  \"height\": ${height},\n"
                    + "  \"material\": \"${material}\",\n"
                    + "  \"sound\": \"${sound}\",\n"
                    + "  \"wingsState\": \"${wingsState}\"\n"
                    + "}")
    );
  }

  /**
   * Валидация ответа при создании утки.
   * @param runner
   * @param responseMessage
   */
  public void validateResponse(TestCaseRunner runner, String responseMessage) {
    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
    );
  }
}