package nl.saxion.act.i7.quitter.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.saxion.act.i7.quitter.Application;

/***
 * A model for a tweet.
 */
public class TweetModel {
    /***
     * The date format from Twitter.
     */
    private static final String TWITTER_DATE = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    /***
     * The ID of the tweet.
     */
    private long id;

    /***
     * The text of the tweet.
     */
    private String text;

    /***
     * The user who posted the tweet.
     */
    private UserModel user;

    /***
     * The date when the tweet was created.
     */
    private Date createdAt;

    /***
     * The number of retweets.
     */
    private int retweetCount;

    /***
     * The number of favorites (likes).
     */
    private int favoriteCount;

    /***
     * A boolean that is true if the current logged in user has this tweet marked as favorite.
     */
    private boolean favorite;

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
            this.favorite = jsonObject.getBoolean("favorited");

            // Get the likes from the original tweet, if this is a retweet.
            if(jsonObject.has("retweeted_status")) {
                JSONObject retweetedStatus = jsonObject.getJSONObject("retweeted_status");
                this.favoriteCount = retweetedStatus.getInt("favorite_count");
            } else {
                this.favoriteCount = jsonObject.getInt("favorite_count");
            }

            // Check for the user if we don't haveone.
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

    /***
     * Get the ID of the tweet.
     *
     * @return The ID of the tweet.
     */
    public long getId() {
        return this.id;
    }

    /***
     * Get the text of the tweet.
     *
     * @return The text of the tweet.
     */
    public String getText() {
        return this.text;
    }

    /***
     * Get the user of the tweet.
     *
     * @return The user of the tweet.
     */
    public UserModel getUser() {
        return this.user;
    }

    /***
     * Get the date when the tweet was created.
     *
     * @return The date when the tweet was created.
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /***
     * Get the number of retweets.
     *
     * @return The number of retweets.
     */
    public int getRetweetCount() {
        return this.retweetCount;
    }

    /***
     * Get the number of favorites.
     *
     * @return The number of favorites.
     */
    public int getFavoriteCount() {
        return this.favoriteCount;
    }

    /***
     * Get the favorite flag.
     *
     * @return True if this tweet is marked as favorite for the logged in user, false otherwise.
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    /***
     * Set the number of favorites.
     *
     * @param favoriteCount The new favorites count.
     */
    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    /***
     * Set the favorite status.
     *
     * @param favorite True if this should be marked as favorite, false otherwise.
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
