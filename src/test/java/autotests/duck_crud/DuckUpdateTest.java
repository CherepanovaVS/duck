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
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckUpdateTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка изменения у утки цвета и высоты.")
  @CitrusTest
  public void successfulUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
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

    runner.variable("color", "red");
    runner.variable("height", 5);
    updateDuck(runner);

    String responseMessage = "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
            + "}";

    validateResponse(runner, responseMessage);
  }

  @Test (description = "Проверка изменения у утки цвета и звука.")
  @CitrusTest
  public void successfulUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
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

    runner.variable("color", "green");
    runner.variable("sound", "QUACK");
    updateDuck(runner);

    String responseMessage = "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
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
   * Изменение параметров утки.
   * @param runner
   */
  public void updateDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .put("/api/duck/update")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("color", "${color}")
            .queryParam("height", "${height}")
            .queryParam("id", "${duckId}")
            .queryParam("material", "${material}")
            .queryParam("sound", "${sound}")
    );
  }

  /**
   * Валидация ответа при изменении параметров утки.
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