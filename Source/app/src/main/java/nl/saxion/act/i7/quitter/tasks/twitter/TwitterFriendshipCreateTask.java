package nl.saxion.act.i7.quitter.tasks.twitter;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

public class TwitterFriendshipCreateTask extends TwitterApiTask<Void> {
    private long userId;

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
