package autotests.duck_crud;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.TestActionBuilder.TYPE_RESOLVER;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckUpdateTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка изменения у утки цвета и высоты.")
  @CitrusTest
  public void successfulUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    updateDuck(runner, "red", 5, "rubber", "quack");
    validateResponse(runner, "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
            + "}");
  }

  @Test (description = "Проверка изменения у утки цвета и звука.")
  @CitrusTest
  public void successfulUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
    createDuck(runner, "yellow", 0.1, "rubber", "quack", "ACTIVE");
    getDuckId(runner);
    updateDuck(runner, "green", 0.1, "rubber", "QUACK");
    validateResponse(runner, "{\n"
            + "  \"message\": \"Duck with id = ${duckId} is updated\"\n"
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
   * Изменение параметров утки.
   * @param runner
   * @param color
   * @param height
   * @param material
   * @param sound
   */
  public void updateDuck(TestCaseRunner runner, String color, double height, String material, String sound) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .put("/api/duck/update")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("color", color)
            .queryParam("height", String.valueOf(height))
            .queryParam("id", "${duckId}")
            .queryParam("material", material)
            .queryParam("sound", sound)
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