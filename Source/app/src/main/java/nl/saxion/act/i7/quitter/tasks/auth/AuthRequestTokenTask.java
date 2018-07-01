package nl.saxion.act.i7.quitter.tasks.auth;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import nl.saxion.act.i7.quitter.tasks.AsyncTask;

/***
 * The async task that get the request token.
 */
public class AuthRequestTokenTask extends AsyncTask<Void, Void, OAuth1RequestToken> {
    /***
     * The OAuth service.
     */
    private OAuth10aService oAuthService;

    /***
     * The constructor.
     *
     * @param oAuthService The OAuth service.
     */
    public AuthRequestTokenTask(OAuth10aService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected OAuth1RequestToken doInBackground(Void... params) {
        try {
            return this.oAuthService.getRequestToken();
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
