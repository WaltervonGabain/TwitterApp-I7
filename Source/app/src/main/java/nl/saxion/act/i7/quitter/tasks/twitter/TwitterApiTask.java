package nl.saxion.act.i7.quitter.tasks.twitter;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.tasks.AsyncTask;

/***
 * The base class for Twitter requests based on {@link AsyncTask}.
 *
 * @param <Result> The result type.
 */
public abstract class TwitterApiTask<Result> extends AsyncTask<Void, Void, Result> {
    /***
     * The API url.
     */
    private static final String API_URL = "https://api.twitter.com/1.1/";

    /***
     * The callback that should be called when an error occur.
     */
    private Runnable onErrorConsumer = null;

    /***
     * Set the error callback.
     *
     * @param runnable The callback that should be called.
     *
     * @return This instance.
     */
    public TwitterApiTask<Result> onError(Runnable runnable) {
        this.onErrorConsumer = runnable;
        return this;
    }

    /***
     * The endpoint of the twitter API.
     *
     * @return The endpoint.
     */
    @WorkerThread
    abstract String getEndpoint();

    /***
     * The method of the API.
     *
     * @return The method, for example GET or POST.
     */
    @WorkerThread
    abstract Verb getMethod();

    /***
     * The callback called when the response is successful.
     *
     * @param response The response.
     *
     * @return The result that need to be passed on the main thread.
     */
    @WorkerThread
    abstract Result onSuccess(String response);

    /***
     * The callback called before the request is signed and executed.
     *
     * @param request The request that will be executed.
     */
    @WorkerThread
    void onPreRequestSign(OAuthRequest request)
    {
    }

    @Override
    protected final Result doInBackground(Void... voids) {
        OAuthRequest request = new OAuthRequest(this.getMethod(), API_URL + this.getEndpoint());

        this.onPreRequestSign(request);

        Response response = AuthManager.getInstance().signAndExecuteRequest(request);
        if (response != null && response.isSuccessful()) {
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
