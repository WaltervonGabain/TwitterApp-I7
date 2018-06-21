package nl.saxion.act.i7.quitter.managers;

import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONObject;

import nl.saxion.act.i7.quitter.models.UserModel;

public class AuthorizationManager {
    public static String CALLBACK_URL = "https://www.quitter.com";

    private static AuthorizationManager instance;

    private OAuth10aService authService;

    private OAuth1AccessToken accessToken;

    private String verifier;

    private OAuth1RequestToken requestToken;

    private UserModel userModel;

    private AuthorizationManager() {
        this.authService = new ServiceBuilder("iYaFaQKtk7MDtIg9oBTgq1vfn")
                .apiSecret("V4RDmqzfd36Mveo43VYcPkWsOsOUsuS72vts5eOfZtrCNCEeoV")
                .callback(CALLBACK_URL)
                .build(TwitterApi.instance());
    }

    public static AuthorizationManager getInstance() {
        if(instance == null) {
            instance = new AuthorizationManager();
        }

        return instance;
    }

    public Response execute(OAuthRequest request) {
        try {
            return this.authService.execute(request);
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    public OAuth1AccessToken getAccessToken() {
        return this.accessToken;
    }

    public String getAuthorizationUrl() {
        return this.authService.getAuthorizationUrl(this.getRequestToken());
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public OAuth1RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                this.requestToken = this.authService.getRequestToken();
            } catch (Exception ex) {
                Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
            }
        }

        return this.requestToken;
    }

    public void handleUserJson(JSONObject jsonObject) {
        this.userModel = new UserModel(jsonObject);
    }

    public void setAccessToken() {
        try {
            this.accessToken = this.authService.getAccessToken(this.requestToken, this.verifier);
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }
    }

    public void setAccessToken(String token, String tokenSecret, String rawResponse) {
        if(!token.isEmpty() && !tokenSecret.isEmpty() && !rawResponse.isEmpty()) {
            this.accessToken = new OAuth1AccessToken(token, tokenSecret, rawResponse);
        }
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public void signRequest(OAuthRequest request) {
        this.authService.signRequest(this.getAccessToken(), request);
    }
}
