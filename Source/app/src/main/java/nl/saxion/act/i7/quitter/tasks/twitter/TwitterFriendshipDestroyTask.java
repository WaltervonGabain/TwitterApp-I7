package nl.saxion.act.i7.quitter.tasks.twitter;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

public class TwitterFriendshipDestroyTask extends TwitterApiTask<Void> {
    private long userId;

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
