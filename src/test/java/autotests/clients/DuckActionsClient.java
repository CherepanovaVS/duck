package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

public class DuckActionsClient extends BaseTest {

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
    getDataFromDbFiveValidations(runner, db, "SELECT * FROM DUCK WHERE ID =" + id,
            "COLOR", color, "HEIGHT", height, "MATERIAL", material,
            "SOUND", sound, "WINGS_STATE", wingsState);
  }

  @Step("Проверка удаленной уточки в БД.")
  public void validateDeletedDuckInDatabase(TestCaseRunner runner, String id) {
    getDataFromDbOneValidation(runner, db,
            "SELECT COUNT(ID) \"Count\" FROM DUCK WHERE ID ="+ id, "Count", "0");
  }

  @Step("Создание уточки в БД.")
  protected void createDuckInDatabase(TestCaseRunner runner, String color, String height, String material,
                                      String sound, String wingsState) {
    updateDataInDb(runner, db, "INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE)\n" +
            "VALUES (${duckId}, '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "');");
  }

  @Step("Удаление уточки в БД.")
  protected void deleteDuckInDatabase(TestCaseRunner runner) {
    updateDataInDb(runner, db, "DELETE FROM DUCK WHERE ID=${duckId}");
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
