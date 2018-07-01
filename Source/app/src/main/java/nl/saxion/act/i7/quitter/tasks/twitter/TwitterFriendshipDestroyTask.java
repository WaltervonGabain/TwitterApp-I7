package nl.saxion.act.i7.quitter.tasks.twitter;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

/***
 * The task to destroy a friendship between logged in user and a specific user.
 */
public class TwitterFriendshipDestroyTask extends TwitterApiTask<Void> {
    /***
     * The ID of the other user.
     */
    private long userId;

    /***
     * The constructor.
     *
     * @param userId The ID of the other user.
     */
    public TwitterFriendshipDestroyTask(long userId) {
        this.userId = userId;
    }

    @Override
    String getEndpoint() {
        return "friendships/destroy.json";
    }

    @Override
    Verb getMethod() {
        return Verb.POST;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("user_id", String.valueOf(this.userId));
    }

    @Override
    Void onSuccess(String response) {
        return null;
    }
}
