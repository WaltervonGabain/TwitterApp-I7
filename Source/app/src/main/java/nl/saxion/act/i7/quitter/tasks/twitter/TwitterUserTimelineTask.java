package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.models.TweetModel;

public class TwitterUserTimelineTask extends TwitterApiTask<ArrayList<TweetModel>> {
    private long userId;

    public TwitterUserTimelineTask(long userId) {
        this.userId = userId;
    }

    @Override
    String getEndpoint() {
        return "statuses/user_timeline.json";
    }

    @Override
    Verb getMethod() {
        return Verb.GET;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("user_id", String.valueOf(this.userId));
        request.addParameter("count", "15");
    }

    @Override
    ArrayList<TweetModel> onSuccess(String response) {
        ArrayList<TweetModel> tweets = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                tweets.add(new TweetModel(jsonArray.getJSONObject(i)));
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return tweets;
    }
}
