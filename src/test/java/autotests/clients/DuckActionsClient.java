package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends BaseTest {

  @Autowired
  protected HttpClient duckService;

  @Autowired
  protected SingleConnectionDataSource db;

  @Description("Создание утки.")
  @Step("Эндпоинт - Создать уточку.")
  public void createDuck(TestCaseRunner runner, Object body) {
    sendPostRequest(runner,
            duckService,
            "/api/duck/create",
            body);
  }

  @Step("Валидация ответа с помощью String.")
  public void validateResponseUsingString(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
    receiveResponse(runner,
            duckService,
            httpStatus,
            responseMessage);
  }

  @Step("Валидация ответа с помощью String.")
  public void validateResponseUsingStringAndExtractVariable(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
    receiveResponseAndExtractVariable(runner,
            duckService,
            httpStatus,
            responseMessage,
            "$.id",
            "duckId");
  }

  @Step("Валидация ответа с помощью Resources.")
  public void validateResponseUsingResources(TestCaseRunner runner, String expectedResource, HttpStatus httpStatus) {
    receiveResponseResourceBody(runner,
            duckService,
            httpStatus,
            new ClassPathResource(expectedResource));
  }

  @Step("Валидация ответа с помощью Payloads.")
  public void validateResponseUsingPayloads(TestCaseRunner runner, Object expectedPayload, HttpStatus httpStatus) {
    receiveResponsePayloadBody(runner,
            duckService,
            httpStatus,
            new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()));
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
    extractVariable(runner,
            duckService,
            "$.id",
            "duckId");
  }

  @Description("Удаление утки.")
  @Step("Эндпоинт - Удалить уточку.")
  public void deleteDuck(TestCaseRunner runner) {
    sendDeleteRequest(runner,
            duckService,
            "/api/duck/delete?id=${duckId}");
  }

  @Description("Действие утки - лететь.")
  @Step("Эндпоинт - Лететь.")
  public void flyDuck(TestCaseRunner runner) {
    sendGetRequest(runner,
            duckService,
            "/api/duck/action/fly?id=${duckId}");
  }

  @Description("Получение характеристик утки.")
  @Step("Эндпоинт - Показать характеристики.")
  public void getPropertiesDuck(TestCaseRunner runner) {
    sendGetRequest(runner,
            duckService,
            "/api/duck/action/properties?id=${duckId}");
  }

  @Description("Действие утки - крякать.")
  @Step("Эндпоинт - Крякать.")
  public void quackDuck(TestCaseRunner runner, String repetitionCount, String soundCount) {
    sendGetRequest(runner,
            duckService,
            "/api/duck/action/quack?id=${duckId}&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount);
  }

  @Description("Действие утки - плыть.")
  @Step("Эндпоинт - Плыть.")
  public void swimDuck(TestCaseRunner runner, String id) {
    sendGetRequest(runner,
            duckService,
            "/api/duck/action/swim?id=" + id);
  }

  @Description("Изменение параметров утки.")
  @Step("Эндпоинт - Обновить харктеристики уточки.")
  public void updateDuck(TestCaseRunner runner, String color, String height, String material, String sound) {
    sendPutRequest(runner,
            duckService,
            "/api/duck/update?id=${duckId}&color=" + color + "&height=" + height + "&material=" + material + "&sound=" + sound);
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
