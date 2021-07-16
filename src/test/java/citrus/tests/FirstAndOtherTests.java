package citrus.tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class FirstAndOtherTests extends TestNGCitrusTestRunner {

    @Autowired
    private HttpClient restClient;
    private TestContext context;

    @Test(description = "Получение информации о пользователе")
    @CitrusTest(name = "Первые тесты")
    public void getTestsActions() {
        this.context = citrus.createTestContext();


        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "   \"data\":{\n" +
                        "      \"id\":2,\n" +
                        "      \"email\":\"janet.weaver@reqres.in\",\n" +
                        "      \"first_name\":\"Janet\",\n" +
                        "      \"last_name\":\"Weaver\",\n" +
                        "      \"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"\n" +
                        "   },\n" +
                        "   \"support\":{\n" +
                        "      \"url\":\"https://reqres.in/#support-heading\",\n" +
                        "      \"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "   }\n" +
                        "}")
        );

    }


    @Test(description = "Получение информации о 404 странице")
    @CitrusTest(name = "Юзера нет")
    public void getUserUnknown() {
        this.context = citrus.createTestContext();


        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/23")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .status(HttpStatus.NOT_FOUND)
                .messageType(MessageType.JSON)
        );

    }

    @Test(description = "Получение информации о ресурсе")
    @CitrusTest(name = "Ресурс найден")
    public void getSingleResource() {
        this.context = citrus.createTestContext();


        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("unknown/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .status(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "    \"data\": {\n" +
                        "        \"id\": 2,\n" +
                        "        \"name\": \"fuchsia rose\",\n" +
                        "        \"year\": 2001,\n" +
                        "        \"color\": \"#C74375\",\n" +
                        "        \"pantone_value\": \"17-2031\"\n" +
                        "    },\n" +
                        "    \"support\": {\n" +
                        "        \"url\": \"https://reqres.in/#support-heading\",\n" +
                        "        \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "    }\n" +
                        "}")
        );

    }


    @Test(description = "Получение информации о ресурсе, которого нет")
    @CitrusTest(name = "Один ресурс не найден")
    public void getSingleResourceIsNotFound() {
        this.context = citrus.createTestContext();


        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("unknown/23")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .status(HttpStatus.NOT_FOUND)
                .messageType(MessageType.JSON)

        );
    }

    @Test(description = "Создание нового юзера")
    @CitrusTest(name = "Создание юзера через пост")
    public void postNewUser() {
        this.context = citrus.createTestContext();


        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .post("users")
                .payload("{\n" +
                        "    \"name\": \"Neo\",\n" +
                        "    \"job\": \"TheOne\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .status(HttpStatus.CREATED)
                .messageType(MessageType.JSON)
                .validate("$.name","Neo")
                .validate("$.job","TheOne")

        );
    }

}
