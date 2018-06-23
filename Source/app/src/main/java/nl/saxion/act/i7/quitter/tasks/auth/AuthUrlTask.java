package nl.saxion.act.i7.quitter.tasks.auth;

import android.util.Log;

import com.github.scribejava.core.oauth.OAuth10aService;

import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.tasks.AsyncTask;

public class AuthUrlTask extends AsyncTask<Void, Void, String> {
    private OAuth10aService oAuthService;

    public AuthUrlTask(OAuth10aService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return this.oAuthService.getAuthorizationUrl(AuthManager.getInstance().getRequestToken());
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
