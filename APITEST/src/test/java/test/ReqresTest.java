package test;

import api.Register;
import api.SuccessReg;
import api.UserData;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.*;

import java.util.List;

public class ReqresTest {

    @Test
    public void GetUserList() {
        // V1.0
        // Get a checking of status-code right in the request
        HttpClient.codeResponseV1_0();

        // V1.1
        // Here we use the response for the checking
        Response response = HttpClient.codeResponseV1_1();
        Assert.assertEquals(response.getStatusCode(), StatusCodeEnum.OK.getValue());

        // V1.2
        // Time to specifications. Status-code is checked with specifications
        // I know, we could create class only with "data" fields but i decided to create the structure with internal static classes
        Specifications.installSpecification(Specifications.requestSpec(Config.URL), Specifications.responseCode(StatusCodeEnum.OK.getValue()));
        List<UserData.Datum> users = HttpClient.getSubClassDataList();
        // it is just for assert example
        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        // Here we get a full-structure response as an object (not list)
        UserData usersWholeStructure = HttpClient.getWholeStructureObject();
        // just for a fun example
        for (UserData.Datum item : usersWholeStructure.getData()) {
            System.out.println(item.getAvatar());
        }

        // No POJO request and response anymore
        Response nonPojo = HttpClient.getResponseAndCheckingInside();
        JsonPath jsonPath = nonPojo.jsonPath();
        // There is no Pojo, only lists + jsonPath
        List<String> avatars = jsonPath.get("data.avatar");
        List<Integer> ids = jsonPath.get("data.id");

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }

    }

    @Test
    public void PostSuccessRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(Config.URL), Specifications.responseCode(StatusCodeEnum.OK.getValue()));
        // It has been created with POJO
        Register user = new Register(TestData.REGISTRATION_EMAIL, TestData.REGISTRATION_PASSWORD);
        SuccessReg successReg = HttpClient.postWholeStructureObject(user);
        Assert.assertNotNull(successReg.getId());
        Assert.assertNotNull(successReg.getToken());
        Assert.assertEquals(TestData.RESPONSE_OF_REGISTRATION_ID, successReg.getId());
        Assert.assertEquals(TestData.RESPONSE_OF_REGISTRATION_TOKEN, successReg.getToken());

        // No POJO
        Response response = HttpClient.postRequestMapDataPlusNotNullInsideResponse();
        JsonPath jsonPath = response.jsonPath();
        // No Lists as well
        Assert.assertEquals(TestData.RESPONSE_OF_REGISTRATION_ID, jsonPath.get("id"));
        Assert.assertEquals(TestData.RESPONSE_OF_REGISTRATION_TOKEN, jsonPath.get("token"));
    }
}