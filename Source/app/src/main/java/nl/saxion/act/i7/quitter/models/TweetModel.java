package nl.saxion.act.i7.quitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.act.i7.quitter.data_providers.UserDataProvider;

public class TweetModel {
    /***
     * The text of the tweet.
     */
    private String text;

    /***
     * The user who posted the tweet.
     */
    private UserModel user;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON of the tweet.
     *
     * @throws JSONException JSON exception.
     */
    public TweetModel(JSONObject jsonObject) throws JSONException {
        this.text = jsonObject.getString("text");
        this.user = UserDataProvider.getOrAdd(jsonObject.getJSONObject("user"));
    }

    /***
     * Get the text of the tweet.
     *
     * @return The text of the tweet.
     */
    public String getText() {
        return text;
    }

    /***
     * Get the user of the tweet.
     *
     * @return The user of the tweet.
     */
    public UserModel getUser() {
        return user;
    }
}
