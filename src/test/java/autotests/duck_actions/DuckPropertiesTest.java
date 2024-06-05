package autotests.duck_actions;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка характеристик утки с нечетным id, material = rubber.")
  @CitrusTest
  public void successfulGetPropertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                               @Optional @CitrusResource TestContext context) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);
    setDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));

    if (duckId % 2 == 0) {
      createDuck(runner);
      setDuckId(runner);
    }

    getPropertiesDuck(runner);

    validateResponse(runner, "{\n"
            + "  \"color\": \"${color}\",\n"
            + "  \"height\": ${height},\n"
            + "  \"material\": \"${material}\",\n"
            + "  \"sound\": \"${sound}\",\n"
            + "  \"wingsState\": \"${wingsState}\"\n"
            + "}");
  }

  @Test (description = "Проверка характеристик утки с четным id, material = wood.")
  @CitrusTest
  public void successfulGetPropertiesEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                            @Optional @CitrusResource TestContext context) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "wood");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);
    setDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));

    if (duckId % 2 != 0) {
      createDuck(runner);
      setDuckId(runner);
    }

    getPropertiesDuck(runner);

    validateResponse(runner, "{\n"
            + "  \"color\": \"${color}\",\n"
            + "  \"height\": ${height},\n"
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
   * Получение характеристик утки.
   * @param runner
   */
  public void getPropertiesDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .get("/api/duck/action/properties")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Валидация ответа при получении характеристик утки.
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

  /**
   * Сохранение id созданной утки.
   * @param runner
   */
  public void setDuckId(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }
}