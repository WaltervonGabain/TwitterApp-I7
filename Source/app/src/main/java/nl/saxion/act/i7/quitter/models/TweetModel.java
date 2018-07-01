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

    private long id;

    /***
     * The text of the tweet.
     */
    private String text;

    /***
     * The user who posted the tweet.
     */
    private UserModel user;

    private Date createdAt;

    private int retweetCount;

    private int favoriteCount;

    private boolean favorited;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON of the tweet.
     * @param user The user of the tweet.
     */
    public TweetModel(JSONObject jsonObject, UserModel user) {
        try {
            this.id = jsonObject.getLong("id");
            this.text = jsonObject.getString("text");
            this.retweetCount = jsonObject.getInt("retweet_count");
            this.favorited = jsonObject.getBoolean("favorited");

            // Get the likes from the original tweet, if this is a retweet.
            if(jsonObject.has("retweeted_status")) {
                JSONObject retweetedStatus = jsonObject.getJSONObject("retweeted_status");
                this.favoriteCount = retweetedStatus.getInt("favorite_count");
            } else {
                this.favoriteCount = jsonObject.getInt("favorite_count");
            }

            if(user == null) {
                JSONObject userJson = jsonObject.getJSONObject("user");

                this.user = Application.getInstance().getUsersManager().get(userJson);
                if (this.user == null) {
                    this.user = Application.getInstance().getUsersManager().add(userJson);
                }
            } else {
                this.user = user;
            }

            this.createdAt = new SimpleDateFormat(TWITTER_DATE, Locale.ENGLISH).parse(jsonObject.getString("created_at"));
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
    }

    public long getId() {
        return id;
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

    public int getRetweetCount() {
        return this.retweetCount;
    }

    public int getFavoriteCount() {
        return this.favoriteCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
