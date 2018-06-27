package nl.saxion.act.i7.quitter.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.saxion.act.i7.quitter.Application;

public class TweetModel {
    private static final String TWITTER_DATE = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    /***
     * The text of the tweet.
     */
    private String text;

    /***
     * The user who posted the tweet.
     */
    private UserModel user;

    private Date createdAt;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON of the tweet.
     *
     * @throws JSONException JSON exception.
     */
    public TweetModel(JSONObject jsonObject) {
        try {
            this.text = jsonObject.getString("text");

            JSONObject userJson = jsonObject.getJSONObject("user");

            this.user = Application.getInstance().getUsersManager().get(userJson);
            if (this.user == null) {
                this.user = Application.getInstance().getUsersManager().add(userJson);
            }

            this.createdAt = new SimpleDateFormat(TWITTER_DATE, Locale.ENGLISH).parse(jsonObject.getString("created_at"));
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
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

    public Date getCreatedAt() {
        return this.createdAt;
    }
}
