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

public class DuckQuackTest extends TestNGCitrusSpringSupport {
  @Test (description = "Проверка действия утки - крякать с нечетным id.")
  @CitrusTest
  public void successfulQuackNotEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                       @Optional @CitrusResource TestContext context) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");
    runner.variable("repetitionCount", 3);
    runner.variable("soundCount", 2);

    createDuck(runner);
    setDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));

    // Если id четный, создадим ещё одну утку, чтобы у неё был нечетный id.
    if (duckId % 2 == 0) {
      createDuck(runner);
      setDuckId(runner);
    }

    quackDuck(runner);

    String responseSound = getResponseSound(context);

    String responseMessage = "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
            + "}";

    validateResponse(runner, responseMessage);
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                    @Optional @CitrusResource TestContext context) {
    runner.variable("color", "yellow");
    runner.variable("height", 0.1);
    runner.variable("material", "rubber");
    runner.variable("sound", "quack");
    runner.variable("wingsState", "ACTIVE");
    runner.variable("repetitionCount", 3);
    runner.variable("soundCount", 2);

    createDuck(runner);
    setDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));

    // Если id нечетный, создадим ещё одну утку, чтобы у неё был четный id.
    if (duckId % 2 != 0) {
      createDuck(runner);
      setDuckId(runner);
    }

    quackDuck(runner);

    String responseSound = getResponseSound(context);

    String responseMessage = "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
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
   * Действие утки - крякать.
   * @param runner
   */
  public void quackDuck(TestCaseRunner runner) {
    runner.$(http().client("http://localhost:2222")
            .send()
            .get("/api/duck/action/quack")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
            .queryParam("repetitionCount", "${repetitionCount}")
            .queryParam("soundCount", "${soundCount}")
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
   * Генерация строки со звуком утки.
   * @param context
   * @return
   */
  public String getResponseSound (TestContext context) {
    String sound = context.getVariable("sound");
    int soundCount = Integer.parseInt(context.getVariable("soundCount"));
    int repetitionCount = Integer.parseInt(context.getVariable("repetitionCount"));
    String responseSound = (sound + "-").repeat(soundCount);
    responseSound = responseSound.substring(0, responseSound.length() - 1);
    responseSound = (responseSound + ", ").repeat(repetitionCount);
    responseSound = responseSound.substring(0, responseSound.length() - 2);

    return responseSound;
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