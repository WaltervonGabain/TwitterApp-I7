package nl.saxion.act.i7.quitter.tasks.twitter;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

/***
 * The task to create a friendship between logged in user and a specific user.
 */
public class TwitterFriendshipCreateTask extends TwitterApiTask<Void> {
    /***
     * The other user.
     */
    private long userId;

    /***
     * The other user.
     *
     * @param userId The user id of the other user.
     */
    public TwitterFriendshipCreateTask(long userId) {
        this.userId = userId;
    }

    @Override
    String getEndpoint() {
        return "friendships/create.json";
    }

    @Override
    Verb getMethod() {
        return Verb.POST;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("user_id", String.valueOf(this.userId));
        request.addParameter("follow", "true");
    }

    @Override
    Void onSuccess(String response) {
        return null;
    }
}
