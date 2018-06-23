package nl.saxion.act.i7.quitter.managers;

import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.reactivex.Single;
import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.tasks.auth.AuthTokenExchangeTask;
import nl.saxion.act.i7.quitter.tasks.auth.AuthUrlTask;
import nl.saxion.act.i7.quitter.tasks.auth.AuthRequestTokenTask;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterVerifyCredentialsTask;

public class AuthManager {
    public final static String CALLBACK_URL = "https://www.quitter.com";

    private final static String apiKey = "iYaFaQKtk7MDtIg9oBTgq1vfn";

    private final static String apiSecret = "V4RDmqzfd36Mveo43VYcPkWsOsOUsuS72vts5eOfZtrCNCEeoV";

    private static AuthManager instance;

    private OAuth10aService oAuthService;

    private OAuth1AccessToken oAuthAccessToken;

    private OAuth1RequestToken requestToken;

    private AuthManager() {
        this.oAuthService = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(CALLBACK_URL)
                .build(TwitterApi.instance());

        SharedPreferencesManager sharedPreferences = SharedPreferencesManager.getInstance();

        String token = sharedPreferences.getString("token", "");
        String tokenSecret = sharedPreferences.getString("tokenSecret", "");
        String rawResponse = sharedPreferences.getString("rawResponse", "");

        if(!token.isEmpty() && !tokenSecret.isEmpty() && !rawResponse.isEmpty()) {
            this.oAuthAccessToken = new OAuth1AccessToken(token, tokenSecret, rawResponse);
        }
    }

    public Response executeRequest(OAuthRequest request) {
        try {
            return this.oAuthService.execute(request);
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    public static AuthManager getInstance() {
        if(instance == null) {
            instance = new AuthManager();
        }

        return instance;
    }

    public Single<String> getAuthorizationUrl() {
        return Single.create((emitter) -> new AuthRequestTokenTask(this.oAuthService)
                .onResult(oAuthRequestToken -> {
                    this.requestToken = oAuthRequestToken;

                    new AuthUrlTask(this.oAuthService)
                            .onResult(emitter::onSuccess)
                            .execute();
                })
                .execute());
    }

    public OAuth1RequestToken getRequestToken() {
        return this.requestToken;
    }

    public boolean hasAccessToken() {
        return this.oAuthAccessToken != null;
    }

    public Single<Boolean> login(String oAuthVerifier) {
        return Single.create(emitter -> {
            Runnable runnable = () -> new TwitterVerifyCredentialsTask()
                    .onError(() -> emitter.onSuccess(false))
                    .onResult((user) -> {
                        if(user != null) {
                            SharedPreferencesManager sharedPreferences = SharedPreferencesManager.getInstance();

                            sharedPreferences.putString("token", this.oAuthAccessToken.getToken());
                            sharedPreferences.putString("tokenSecret", this.oAuthAccessToken.getTokenSecret());
                            sharedPreferences.putString("rawResponse", this.oAuthAccessToken.getRawResponse());
                            sharedPreferences.apply();

                            Application.getInstance().setCurrentUser(user);
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    })
                    .execute();

            if (oAuthVerifier != null && !oAuthVerifier.isEmpty()) {
                new AuthTokenExchangeTask(this.oAuthService, this.requestToken, oAuthVerifier)
                        .onResult((accessToken) -> {
                            this.oAuthAccessToken = accessToken;
                            runnable.run();
                        })
                        .execute();
            } else if (this.oAuthAccessToken != null) {
                runnable.run();
            } else {
                emitter.onSuccess(false);
            }
        });
    }

    public void logout() {
        this.oAuthAccessToken = null;
        Application.getInstance().setCurrentUser(null);
    }

    public void signRequest(OAuthRequest request) {
        this.oAuthService.signRequest(this.oAuthAccessToken, request);
    }
}
