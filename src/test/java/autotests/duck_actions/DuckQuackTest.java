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
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    int soundCount = 2;
    int repetitionCount = 3;
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    // Если id четный, создадим ещё одну утку, чтобы у неё был нечетный id.
    if (duckId % 2 == 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    quackDuck(runner, soundCount, repetitionCount);

    String responseSound = getResponseSound(sound, soundCount, repetitionCount);
    validateResponse(runner, "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
            + "}");
  }

  @Test (description = "Проверка действия утки - крякать с четным id.")
  @CitrusTest
  public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner,
                                    @Optional @CitrusResource TestContext context) {
    String color = "yellow";
    double height = 0.1;
    String material = "rubber";
    String sound = "quack";
    String wingsState = "ACTIVE";
    int soundCount = 2;
    int repetitionCount = 3;
    createDuck(runner, color, height, material, sound, wingsState);
    getDuckId(runner);

    Long duckId = Long.valueOf(context.getVariable("duckId"));
    // Если id нечетный, создадим ещё одну утку, чтобы у неё был четный id.
    if (duckId % 2 != 0) {
      createDuck(runner, color, height, material, sound, wingsState);
      getDuckId(runner);
    }

    quackDuck(runner, soundCount, repetitionCount);

    String responseSound = getResponseSound(sound, soundCount, repetitionCount);
    validateResponse(runner, "{\n"
            + "  \"sound\": \"" + responseSound + "\"\n"
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
   * Действие утки - крякать.
   * @param runner
   * @param repetitionCount
   * @param soundCount
   */
  public void quackDuck(TestCaseRunner runner, int repetitionCount, int soundCount) {
    runner.$(http().client("http://localhost:2222")
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