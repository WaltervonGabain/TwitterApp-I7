package nl.saxion.act.i7.quitter.tasks.auth;

import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import nl.saxion.act.i7.quitter.tasks.AsyncTask;

public class AuthTokenExchangeTask extends AsyncTask<Void, Void, OAuth1AccessToken> {
    private OAuth10aService oAuthService;

    private OAuth1RequestToken requestToken;

    private String verifier;

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
