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

public class DuckSwimTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка действия утки - плыть с существующим id.")
  @CitrusTest
  public void successfulSwimExistId(@Optional @CitrusResource TestCaseRunner runner) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");

    createDuck(runner);
    setDuckId(runner);

    swimDuck(runner, "${duckId}");

    String responseMessage = "{\n"
            + "  \"message\": \"I'm swimming\"\n"
            + "}";

    validateResponseOk(runner, responseMessage);
  }

  @Test (description = "Проверка действия утки - плыть с несуществующим id.")
  @CitrusTest
  public void successfulSwimNotExistId(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "FIXED");

    createDuck(runner);
    setDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    // +100, чтобы получить несуществующий id. Но так как тесты запускаются параллельно, нужно учесть количество
    // тестов, чтобы точно получить несуществующий id.
    context.setVariable("notExistDuckId", duckId + 100);

    swimDuck(runner, "${notExistDuckId}");

    String responseMessage = "{\n"
            + "  \"message\": \"Duck with id = ${notExistDuckId} isn't found\"\n"
            + "}";

    validateResponseNotFound(runner, responseMessage);
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
   * Действие утки - плыть.
   * @param runner
   */
  public void swimDuck(TestCaseRunner runner, String query) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .get("/api/duck/action/swim")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", query)
    );
  }

  /**
   * Валидация ответа.
   * @param runner
   * @param responseMessage
   */
  public void validateResponseOk(TestCaseRunner runner, String responseMessage) {
    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
    );
  }

  /**
   * Валидация ответа.
   * @param runner
   * @param responseMessage
   */
  public void validateResponseNotFound(TestCaseRunner runner, String responseMessage) {
    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.NOT_FOUND)
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