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
import nl.saxion.act.i7.quitter.tasks.auth.AuthRequestTokenTask;
import nl.saxion.act.i7.quitter.tasks.auth.AuthTokenExchangeTask;
import nl.saxion.act.i7.quitter.tasks.auth.AuthUrlTask;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterVerifyCredentialsTask;

/***
 * The auth manager singleton.
 */
public class AuthManager {
    /***
     * The instance of the class.
     */
    private static AuthManager instance;

    /***
     * The API key.
     */
    private String apiKey = "iYaFaQKtk7MDtIg9oBTgq1vfn";

    /***
     * The API secret.
     */
    private String apiSecret = "V4RDmqzfd36Mveo43VYcPkWsOsOUsuS72vts5eOfZtrCNCEeoV";

    /***
     * The callback URL used for OAuth.
     */
    private String callbackUrl = "https://www.quitter.com";

    /***
     * The OAuth service.
     */
    private OAuth10aService oAuthService;

    /***
     * The OAuth access token.
     */
    private OAuth1AccessToken oAuthAccessToken;

    /***
     * The OAuth request token.
     */
    private OAuth1RequestToken requestToken;

    /***
     * Get instance of the singleton.
     *
     * @return The instance of this class.
     */
    public static AuthManager getInstance() {
        if(instance == null) {
            instance = new AuthManager();
        }

        return instance;
    }

    /***
     * The constructor.
     */
    private AuthManager() {
        this.oAuthService = new ServiceBuilder(this.apiKey)
                .apiSecret(this.apiSecret)
                .callback(this.callbackUrl)
                .build(TwitterApi.instance());

        SharedPreferencesManager sharedPreferences = SharedPreferencesManager.getInstance();

        String token = sharedPreferences.getString("token", "");
        String tokenSecret = sharedPreferences.getString("tokenSecret", "");
        String rawResponse = sharedPreferences.getString("rawResponse", "");

        // If there are not empty, then user logged in last time.
        if(!token.isEmpty() && !tokenSecret.isEmpty() && !rawResponse.isEmpty()) {
            this.oAuthAccessToken = new OAuth1AccessToken(token, tokenSecret, rawResponse);
        }
    }

    /***
     * Sign and execute a request.
     *
     * @param request The request to sign and execute.
     *
     * @return The response after the request is executed or null if it fails.
     */
    public Response signAndExecuteRequest(OAuthRequest request) {
        try {
            this.signRequest(request);
            return this.oAuthService.execute(request);
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    /***
     * Get the authorization URL in an asynchronous method.
     *
     * @return A {@link Single} that emmit the URL when the URL is received.
     */
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

    /***
     * Get the callback URL.
     *
     * @return The callback URL.
     */
    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    /***
     * Get the request token.
     *
     * @return The request token.
     */
    public OAuth1RequestToken getRequestToken() {
        return this.requestToken;
    }

    /***
     * Check if there is an access token.
     *
     * @return True if we there is an access token, false otherwise.
     */
    public boolean hasAccessToken() {
        return this.oAuthAccessToken != null;
    }

    /***
     * Log in the user in an asynchronous method (Token exchange and verify credentials are done here).
     *
     * @param oAuthVerifier The OAuth verifier.
     *
     * @return A {@link Single} that emmit true if the user is logged in correctly, false otherwise.
     */
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

                            Application.getInstance().getUsersManager().setCurrentUser(user);
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

    /***
     * Log out the current user.
     */
    public void logout() {
        this.oAuthAccessToken = null;

        UsersManager usersManager = Application.getInstance().getUsersManager();

        usersManager.setCurrentUser(null);
        usersManager.clear();
    }

    /***
     * Sign a request.
     *
     * @param request The signed request.
     */
    private void signRequest(OAuthRequest request) {
        this.oAuthService.signRequest(this.oAuthAccessToken, request);
    }
}
