package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

  @Autowired
  protected HttpClient duckService;

  @Autowired
  protected SingleConnectionDataSource db;

  @Description("Создание утки.")
  @Step("Эндпоинт - Создать уточку.")
  public void createDuck(TestCaseRunner runner, Object body) {
    runner.$(http().client(duckService)
            .send()
            .post("/api/duck/create")
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
    );
  }

  @Step("Валидация ответа с помощью String.")
  public void validateResponseUsingString(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
    runner.$(http()
            .client(duckService)
            .receive()
            .response(httpStatus)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(responseMessage)
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }

  @Step("Валидация ответа с помощью Resources.")
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

  @Step("Валидация ответа с помощью Payloads.")
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

  @Step("Валидация параметров уточки в БД.")
  public void validateDuckInDatabase(TestCaseRunner runner, String id, String color, String height, String material,
                                        String sound, String wingsState) {
    runner.$(query(db)
            .statement("SELECT * FROM DUCK WHERE ID =" + id)
            .validate("COLOR", color)
            .validate("HEIGHT", height)
            .validate("MATERIAL", material)
            .validate("SOUND", sound)
            .validate("WINGS_STATE", wingsState)
    );
  }

  @Step("Проверка удаленной уточки в БД.")
  public void validateDeletedDuckInDatabase(TestCaseRunner runner, String id) {
    runner.$(query(db)
            .statement("SELECT COUNT(ID) \"Count\" FROM DUCK WHERE ID =" + id)
            .validate("Count", "0")
    );
  }

  @Step("Создание уточки в БД.")
  protected void createDuckInDatabase(TestCaseRunner runner, String color, String height, String material,
                                      String sound, String wingsState) {
    runner.$(sql(db)
            .statement("INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE)\n" +
                    "VALUES (${duckId}, '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "');"));
  }

  @Step("Удаление уточки в БД.")
  protected void deleteDuckInDatabase(TestCaseRunner runner) {
    runner.$(sql(db)
            .statement("DELETE FROM DUCK WHERE ID=${duckId}"));
  }

  @Step("Сохранение id созданной утки.")
  public void getDuckId(TestCaseRunner runner) {
    runner.$(http().client(duckService)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression("$.id", "duckId"))
    );
  }

  @Description("Удаление утки.")
  @Step("Эндпоинт - Удалить уточку.")
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
  @Step("Эндпоинт - Лететь.")
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
  @Step("Эндпоинт - Показать характеристики.")
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
  @Step("Эндпоинт - Крякать.")
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
  @Step("Эндпоинт - Плыть.")
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
  @Step("Эндпоинт - Обновить харктеристики уточки.")
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

  @Step("Генерация строки со звуком утки.")
  public String getResponseSound (String sound, int soundCount, int repetitionCount) {
    String responseSound = (sound + "-").repeat(soundCount);
    responseSound = responseSound.substring(0, responseSound.length() - 1);
    responseSound = (responseSound + ", ").repeat(repetitionCount);
    responseSound = responseSound.substring(0, responseSound.length() - 2);

    return responseSound;
  }
}
