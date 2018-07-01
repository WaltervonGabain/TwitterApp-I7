package nl.saxion.act.i7.quitter.tasks.auth;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import nl.saxion.act.i7.quitter.tasks.AsyncTask;

/***
 * The task that get the access token.
 */
public class AuthTokenExchangeTask extends AsyncTask<Void, Void, OAuth1AccessToken> {
    /***
     * The OAuth service.
     */
    private OAuth10aService oAuthService;

    /***
     * The OAuth request token.
     */
    private OAuth1RequestToken requestToken;

    /***
     * The OAuth verifier.
     */
    private String verifier;

    /***
     * The constructor.
     *
     * @param oAuthService The OAuth service.
     * @param requestToken The OAuth request token.
     * @param verifier The OAuth verifier.
     */
    public AuthTokenExchangeTask(OAuth10aService oAuthService, OAuth1RequestToken requestToken, String verifier) {
        this.oAuthService = oAuthService;
        this.requestToken = requestToken;
        this.verifier = verifier;
    }

    @Override
    protected OAuth1AccessToken doInBackground(Void... parameters) {
        try {
            return this.oAuthService.getAccessToken(this.requestToken, this.verifier);
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
