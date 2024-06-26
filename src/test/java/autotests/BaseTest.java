package autotests;

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
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

  @Autowired
  protected HttpClient duckService;

  @Autowired
  protected SingleConnectionDataSource db;

  @Description("Отправить get запрос.")
  protected void sendGetRequest(TestCaseRunner runner, HttpClient url, String path) {
    runner.$(http()
            .client(url)
            .send()
            .get(path)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE));
  }

  @Description("Отправить post запрос.")
  protected void sendPostRequest(TestCaseRunner runner, HttpClient url, String path, Object body) {
    runner.$(http()
            .client(url)
            .send()
            .post(path)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
    );
  }

  @Description("Отправить delete запрос.")
  protected void sendDeleteRequest(TestCaseRunner runner, HttpClient url, String path) {
    runner.$(http()
            .client(url)
            .send()
            .delete(path)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
    );
  }

  @Description("Отправить put запрос.")
  protected void sendPutRequest(TestCaseRunner runner, HttpClient url, String path) {
    runner.$(http()
            .client(url)
            .send()
            .put(path)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
    );
  }

  @Description("Получить ответ, использую для валидации Resource.")
  protected void receiveResponseResourceBody(TestCaseRunner runner, HttpClient url, HttpStatus httpStatus,
                                             ClassPathResource body) {
    runner.$(http()
            .client(url)
            .receive()
            .response(httpStatus)
            .message()
            .type(MessageType.JSON)
            .body(body)
    );
  }

  @Description("Получить ответ, использую для валидации Payload.")
  protected void receiveResponsePayloadBody(TestCaseRunner runner, HttpClient url, HttpStatus httpStatus,
                                            ObjectMappingPayloadBuilder body) {
    runner.$(http()
            .client(url)
            .receive()
            .response(httpStatus)
            .message()
            .type(MessageType.JSON)
            .body(body)
    );
  }

  @Description("Получить ответ и записать данные в переменную")
  protected void receiveResponseAndExtractVariable(TestCaseRunner runner, HttpClient url, HttpStatus httpStatus,
                                                    String responseMessage, String variableValue, String variableName) {
    runner.$(http()
            .client(url)
            .receive()
            .response(httpStatus)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(responseMessage)
            .extract(fromBody().expression(variableValue, variableName))
    );
  }

  @Description("Получить ответ.")
  protected void receiveResponse(TestCaseRunner runner, HttpClient url, HttpStatus httpStatus, String responseMessage) {
    runner.$(http()
            .client(url)
            .receive()
            .response(httpStatus)
            .message()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(responseMessage)
    );
  }

  @Description("Запись данных из ответа в переменную.")
  protected void extractVariable(TestCaseRunner runner, HttpClient url, String variableValue, String variableName) {
    runner.$(http()
            .client(url)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .extract(fromBody().expression(variableValue, variableName))
    );
  }

  @Description("Изменение, добавление или удаление данных в БД.")
  protected void updateDataInDb(TestCaseRunner runner, SingleConnectionDataSource dataSource, String sql) {
    runner.$(sql(dataSource).statement(sql));
  }

  @Description("Получение данных из БД и валидация одного значения.")
  protected void getDataFromDbOneValidation(TestCaseRunner runner, SingleConnectionDataSource dataSource, String sql,
                               String column, String value) {
    runner.$(query(dataSource)
            .statement(sql)
            .validate(column, value));
  }

  @Description("Получение данных из БД и валидация 5 значений.")
  protected void getDataFromDbFiveValidations(TestCaseRunner runner, SingleConnectionDataSource dataSource, String sql,
                                              String columnOne, String valueOne, String columnTwo, String valueTwo,
                                              String columnThree, String valueThree, String columnFour, String valueFour,
                                              String columnFive, String valueFive) {
    runner.$(query(dataSource)
            .statement(sql)
            .validate(columnOne, valueOne)
            .validate(columnTwo, valueTwo)
            .validate(columnThree, valueThree)
            .validate(columnFour, valueFour)
            .validate(columnFive, valueFive)
    );
  }
}
