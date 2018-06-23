package nl.saxion.act.i7.quitter.tasks.twitter;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.tasks.AsyncTask;

public abstract class TwitterApiTask<Result> extends AsyncTask<Void, Void, Result> {
    private static final String API_URL = "https://api.twitter.com/1.1/";

    private Runnable onErrorConsumer = null;

    public TwitterApiTask<Result> onError(Runnable runnable) {
        this.onErrorConsumer = runnable;
        return this;
    }

    @WorkerThread
    abstract String getEndpoint();

    @WorkerThread
    abstract Verb getMethod();

    @WorkerThread
    abstract Result onSuccess(String response);

    @WorkerThread
    void onPreRequestSign(OAuthRequest request)
    {
    }

    @Override
    protected final Result doInBackground(Void... voids) {
        OAuthRequest request = new OAuthRequest(this.getMethod(), API_URL + this.getEndpoint());

        this.onPreRequestSign(request);
        AuthManager.getInstance().signRequest(request);

        Response response = AuthManager.getInstance().executeRequest(request);
        if (response.isSuccessful()) {
            try {
                return this.onSuccess(response.getBody());
            } catch (Exception ex) {
                Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
            }
        } else {
            if (this.onErrorConsumer != null) {
                this.onErrorConsumer.run();
            }
        }

        return null;
    }
}
