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

public class DuckDeleteTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка удаления утки.")
  @CitrusTest
  public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
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

    deleteDuck(runner);

    String responseMessage = "{\n"
            + "  \"message\": \"Duck is deleted\"\n"
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
   * Удаление утки.
   * @param runner
   */
  public void deleteDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .delete("/api/duck/delete")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Валидация ответа при удалении утки.
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