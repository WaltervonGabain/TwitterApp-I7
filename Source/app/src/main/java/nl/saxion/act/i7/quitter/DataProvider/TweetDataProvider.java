package nl.saxion.act.i7.quitter.DataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.Models.TweetModel;

public class TweetDataProvider {
    private static ArrayList<TweetModel> tweets = new ArrayList<>();

    /***
     * Remove the tweets and reload them.
     *
     * @param jsonObject The JSON file content.
     *
     * @throws JSONException JSON exception.
     */
    public static void reload(JSONObject jsonObject) throws JSONException {
        tweets.clear();

        JSONArray jsonArray = jsonObject.getJSONArray("statuses");

        for(int i = 0; i < jsonArray.length(); i++) {
            tweets.add(new TweetModel(jsonArray.getJSONObject(i)));
        }
    }

    /***
     * Get all tweets.
     *
     * @return An array of tweets.
     */
    public static ArrayList<TweetModel> getAll() {
        return tweets;
    }
}
