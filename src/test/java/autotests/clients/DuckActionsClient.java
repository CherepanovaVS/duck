package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

  @Autowired
  protected HttpClient duckService;

  @Description("Создание утки.")
  public void createDuck(TestCaseRunner runner, Object body) {
    runner.$(http().client(duckService)
            .send()
            .post("/api/duck/create")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
    );
  }

  @Description("Валидация ответа с помощью String.")
  public void validateResponseUsingString(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
    runner.$(http()
            .client(duckService)
            .receive()
            .response(httpStatus)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(responseMessage)
    );
  }

  @Description("Валидация ответа с помощью Resources.")
  public void validateResponseUsingResources(TestCaseRunner runner, String expectedResource, HttpStatus httpStatus) {
    runner.$(http()
            .client(duckService)
            .receive()
            .response(httpStatus)
            .message()
            .type(MessageType.JSON)
            .body(new ClassPathResource(expectedResource))
    );
  }

  @Description("Валидация ответа с помощью Payloads.")
  public void validateResponseUsingPayloads(TestCaseRunner runner, Object expectedPayload, HttpStatus httpStatus) {
    runner.$(http()
            .client(duckService)
            .receive()
            .response(httpStatus)
            .message()
            .type(MessageType.JSON)
            .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
    );
  }

  @Description("Сохранение id созданной утки.")
  public void getDuckId(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }

  @Description("Удаление утки.")
  public void deleteDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .delete("/api/duck/delete")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  @Description("Действие утки - лететь.")
  public void flyDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/fly")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  @Description("Получение характеристик утки.")
  public void getPropertiesDuck(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/properties")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
    );
  }

  @Description("Действие утки - крякать.")
  public void quackDuck(TestCaseRunner runner, String repetitionCount, String soundCount) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/quack")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", "${duckId}")
            .queryParam("repetitionCount", repetitionCount)
            .queryParam("soundCount", soundCount)
    );
  }

  @Description("Действие утки - плыть.")
  public void swimDuck(TestCaseRunner runner, String id) {
    runner.$(http().client(duckService)
            .send()
            .get("/api/duck/action/swim")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("id", id)
    );
  }

  @Description("Изменение параметров утки.")
  public void updateDuck(TestCaseRunner runner, String color, String height, String material, String sound) {
    runner.$(http().client(duckService)
            .send()
            .put("/api/duck/update")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("color", color)
            .queryParam("height", height)
            .queryParam("id", "${duckId}")
            .queryParam("material", material)
            .queryParam("sound", sound)
    );
  }

  @Description("Генерация строки со звуком утки.")
  public String getResponseSound (String sound, int soundCount, int repetitionCount) {
    String responseSound = (sound + "-").repeat(soundCount);
    responseSound = responseSound.substring(0, responseSound.length() - 1);
    responseSound = (responseSound + ", ").repeat(repetitionCount);
    responseSound = responseSound.substring(0, responseSound.length() - 2);

    return responseSound;
  }
}
