package autotests.tests;

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
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    flyDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"message\": \"I'm flying\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - лететь со связанными крыльями.")
  @CitrusTest
  public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "FIXED");
    getDuckId(runner);
    flyDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"message\": \"I can't fly\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - лететь с неопределенным состоянием крыльев.")
  @CitrusTest
  public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "UNDEFINED");
    getDuckId(runner);
    flyDuck(runner);
    validateResponse(runner, "{\n"
            + "  \"message\": \"Wings are not detected :(\"\n"
            + "}");
  }

  /**
   * Создание утки.
   * @param runner
   * @param color
   * @param height
   * @param material
   * @param sound
   * @param wingsState
   */
  public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .post("/api/duck/create")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\n"
                    + "  \"color\": \"" + color + "\",\n"
                    + "  \"height\": " + height + ",\n"
                    + "  \"material\": \"" + material + "\",\n"
                    + "  \"sound\": \"" + sound + "\",\n"
                    + "  \"wingsState\": \"" + wingsState + "\"\n"
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

  /**
   * Сохранение id созданной утки.
   * @param runner
   */
  public void getDuckId(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }
}