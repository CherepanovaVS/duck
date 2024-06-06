package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

  @Autowired
  protected HttpClient duckService;

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
    runner.$(http().client(duckService)
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
   * Валидация OK ответа.
   * @param runner
   * @param responseMessage
   */
  public void validateOkResponse(TestCaseRunner runner, String responseMessage) {
    runner.$(http().client(duckService)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
    );
  }

  /**
   * Валидация Not Found ответа.
   * @param runner
   * @param responseMessage
   */
  public void validateNotFoundResponse(TestCaseRunner runner, String responseMessage) {
    runner.$(http().client(duckService)
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
  public void getDuckId(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }

  /**
   * Удаление утки.
   * @param runner
   */
  public void deleteDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .delete("/api/duck/delete")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Действие утки - лететь.
   * @param runner
   */
  public void flyDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/fly")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Получение характеристик утки.
   * @param runner
   */
  public void getPropertiesDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/properties")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  /**
   * Действие утки - крякать.
   * @param runner
   * @param repetitionCount
   * @param soundCount
   */
  public void quackDuck(TestCaseRunner runner, int repetitionCount, int soundCount) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/quack")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
            .queryParam("repetitionCount", String.valueOf(repetitionCount))
            .queryParam("soundCount", String.valueOf(soundCount))
    );
  }

  /**
   * Действие утки - плыть.
   * @param runner
   */
  public void swimDuck(TestCaseRunner runner, String query) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/swim")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", query)
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
    runner.$(http().client(duckService)
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
   * Генерация строки со звуком утки.
   * @param sound
   * @param soundCount
   * @param repetitionCount
   * @return
   */
  public String getResponseSound (String sound, int soundCount, int repetitionCount) {
    String responseSound = (sound + "-").repeat(soundCount);
    responseSound = responseSound.substring(0, responseSound.length() - 1);
    responseSound = (responseSound + ", ").repeat(repetitionCount);
    responseSound = responseSound.substring(0, responseSound.length() - 2);

    return responseSound;
  }
}
