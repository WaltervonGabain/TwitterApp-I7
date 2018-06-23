package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.Verb;

import org.json.JSONObject;

import nl.saxion.act.i7.quitter.models.UserModel;

public class TwitterVerifyCredentialsTask extends TwitterApiTask<JSONObject> {
    @Override
    protected String getEndpoint() {
        return "account/verify_credentials.json";
    }

    @Override
    protected Verb getMethod() {
        return Verb.GET;
    }

    @Override
    protected JSONObject onSuccess(String response) {
        try {
            return new JSONObject(response);
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
