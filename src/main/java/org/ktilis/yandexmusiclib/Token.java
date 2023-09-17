package org.ktilis.yandexmusiclib;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Token {
    private static @Getter @Setter String token = "";
    private static @Getter @Setter Integer userId;
    private static final String GetTokenUrl = "https://oauth.yandex.com/token";

    /**
     * Warning: This method does not work. Use setToken(String token) with your token.
     * @param login Yandex login
     * @param password Yandex password
     * @return token
     */
    @Deprecated
    public static String getTokenWithPassword(String login, String password) {
        JSONObject obj;
        try {
            obj = NetworkManager.postReq(GetTokenUrl, "grant_type=password&client_id=23cabbbdc6cd418abb4b39c32c41195d&client_secret=53bc75238f0c4d08a118e51fe9203300&username="+login+"&password="+password).get();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String token = obj.getString("access_token");
        Token.token = token;
        Token.userId = obj.getInt("uid");

        return token;
    }


}
