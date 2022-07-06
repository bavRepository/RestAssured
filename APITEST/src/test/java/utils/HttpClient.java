package utils;

import api.Register;
import api.SuccessReg;
import api.UserData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class HttpClient {

    public static void codeResponseV1_0() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(Config.URL + Config.ROUTE_GET_USERS_LIST)
                .then().log().all().assertThat().statusCode(StatusCodeEnum.OK.getValue());
    }

    public static Response codeResponseV1_1() {
        return given()
                .when()
                .contentType(ContentType.JSON)
                .get(Config.URL + Config.ROUTE_GET_USERS_LIST)
                .then().log().all().extract().response();
    }

    public static List<UserData.Datum> getSubClassDataList() {
        return given()
                .when()
                .get(Config.ROUTE_GET_USERS_LIST)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.Datum.class);
    }

    public static UserData getWholeStructureObject() {
        return given()
                .when()
                .get(Config.ROUTE_GET_USERS_LIST)
                .then().log().all()
                // we can also use .extract().as(UserData2.class);
                // instead .extract().body().jsonPath().getObject(".", UserData2.class);
                .extract().body().jsonPath().getObject(".", UserData.class);
    }

    public static Response getResponseAndCheckingInside() {
        return given()
                .when()
                .get(Config.ROUTE_GET_USERS_LIST)
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();
    }

    public static SuccessReg postWholeStructureObject(Register user) {
        return given()
                .body(user)
                .when()
                .post(Config.ROUTE_USER_REGISTRATION)
                .then().log().all()
                .extract().as(SuccessReg.class);
    }

    public static Response postRequestMapDataPlusNotNullInsideResponse() {
        return given()
                .body(BodyRequestData.getbodyRegistrationData())
                .when()
                .post(Config.ROUTE_USER_REGISTRATION)
                .then().log().all()
                .body("id", equalTo(TestData.RESPONSE_OF_REGISTRATION_ID))
                .body("token", equalTo(TestData.RESPONSE_OF_REGISTRATION_TOKEN))
                .extract().response();
    }
}