package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.models.TweetModel;
import nl.saxion.act.i7.quitter.models.UserModel;

public class TwitterUsersSearchTask extends TwitterApiTask<ArrayList<TweetModel>> {
    private String query;

    public TwitterUsersSearchTask(String query) {
        this.query = query;
    }

    @Override
    String getEndpoint() {
        return "users/search.json";
    }

    @Override
    Verb getMethod() {
        return Verb.GET;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("q", this.query);
        request.addParameter("count", "20");
    }

    @Override
    ArrayList<TweetModel> onSuccess(String response) {
        ArrayList<TweetModel> tweets = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userJson = jsonArray.getJSONObject(i);

                UserModel user = Application.getInstance().getUsersManager().get(userJson);
                if (user == null) {
                    user = Application.getInstance().getUsersManager().add(userJson);
                }

                tweets.add(new TweetModel(userJson.getJSONObject("status"), user));
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return tweets;
    }
}
