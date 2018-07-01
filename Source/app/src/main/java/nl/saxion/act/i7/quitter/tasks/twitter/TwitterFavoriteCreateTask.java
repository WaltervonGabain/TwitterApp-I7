package nl.saxion.act.i7.quitter.tasks.twitter;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

/***
 * The task to mark a tweet as favorite (liked).
 */
public class TwitterFavoriteCreateTask extends TwitterApiTask<Void> {
    /***
     * The ID of the tweet.
     */
    private long tweetId;

    /***
     * The constructor
     *
     * @param tweetId The ID of the tweet.
     */
    public TwitterFavoriteCreateTask(long tweetId) {
        this.tweetId = tweetId;
    }

    @Override
    String getEndpoint() {
        return "favorites/create.json";
    }

    @Override
    Verb getMethod() {
        return Verb.POST;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("id", String.valueOf(this.tweetId));
    }

    @Override
    Void onSuccess(String response) {
        return null;
    }
}
