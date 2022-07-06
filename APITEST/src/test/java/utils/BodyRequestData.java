package utils;

import java.util.HashMap;
import java.util.Map;

public class BodyRequestData {

    private static Map<String, Object> bodyUserData;

    public static Map getbodyRegistrationData() {
        bodyUserData = new HashMap<>();
        bodyUserData.put("email", TestData.REGISTRATION_EMAIL);
        bodyUserData.put("password", TestData.REGISTRATION_PASSWORD);
        return bodyUserData;
    }

}
