package autotests.duck_actions;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckFlyTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка действия утки - лететь с активными крыльями.")
  @CitrusTest
  public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);

    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );

    flyDuck(runner);

    String responseMessage = "{\n"
            + "  \"message\": \"I'm flying\"\n"
            + "}";

    validateResponse(runner, responseMessage);
  }

  @Test (description = "Проверка действия утки - лететь со связанными крыльями.")
  @CitrusTest
  public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "FIXED");

    createDuck(runner);

    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );

    flyDuck(runner);

    String responseMessage = "{\n"
            + "  \"message\": \"I can't fly\"\n"
            + "}";

    validateResponse(runner, responseMessage);
  }

  @Test (description = "Проверка действия утки - лететь с неопределенным состоянием крыльев.")
  @CitrusTest
  public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "UNDEFINED");

    createDuck(runner);

    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );

    flyDuck(runner);

    // Здесь я указала фактический результат, так как в документации нет такого состояния крыльев.
    // Когда обсуждали это с Еленой, она сказала, что в целом нельзя содать утку с неопределенным состоянием крыльев,
    // поэтому возможно такого кейса не должно быть для действия fly.
    String responseMessage = "{\n"
            + "  \"message\": \"Wings are not detected :(\"\n"
            + "}";

    validateResponse(runner, responseMessage);
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
   * Действие утки - лететь.
   * @param runner
   */
  public void flyDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .get("/api/duck/action/fly")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Валидация ответа.
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