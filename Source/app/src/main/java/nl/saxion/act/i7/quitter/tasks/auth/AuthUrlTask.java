package nl.saxion.act.i7.quitter.tasks.auth;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.tasks.AsyncTask;

/***
 * The task that gets the auth URL.
 */
public class AuthUrlTask extends AsyncTask<Void, Void, String> {
    /***
     * The OAuth service.
     */
    private OAuth10aService oAuthService;

    /***
     * The constructor.
     *
     * @param oAuthService The OAuth service.
     */
    public AuthUrlTask(OAuth10aService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            OAuth1RequestToken requestToken =AuthManager.getInstance().getRequestToken();
            if (requestToken != null) {
                return this.oAuthService.getAuthorizationUrl(requestToken);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }

        return "";
    }
}
