package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONObject;

import nl.saxion.act.i7.quitter.models.TweetModel;

/***
 * The task that post a new tweet.
 */
public class TwitterUpdateStatusTask extends TwitterApiTask<TweetModel> {
    /***
     * The text of the tweet.
     */
    private String status;

    /***
     * The constructor.
     *
     * @param status The text of the tweet.
     */
    public TwitterUpdateStatusTask(String status) {
        this.status = status;
    }

    @Override
    String getEndpoint() {
        return "statuses/update.json";
    }

    @Override
    Verb getMethod() {
        return Verb.POST;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("status", this.status);
    }

    @Override
    TweetModel onSuccess(String response) {
        try {
            return new TweetModel(new JSONObject(response), null);
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
