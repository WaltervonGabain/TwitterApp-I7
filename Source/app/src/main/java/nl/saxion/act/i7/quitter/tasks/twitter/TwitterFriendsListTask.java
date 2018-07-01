package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.models.TweetModel;
import nl.saxion.act.i7.quitter.models.UserModel;

/***
 * The task to show what logged in user is following.
 */
public class TwitterFriendsListTask extends TwitterApiTask<ArrayList<UserModel>> {
    @Override
    String getEndpoint() {
        return "friends/list.json";
    }

    @Override
    Verb getMethod() {
        return Verb.GET;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        request.addParameter("skip_status", "true");
    }

    @Override
    ArrayList<UserModel> onSuccess(String response) {
        ArrayList<UserModel> users = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("users");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userJson = jsonArray.getJSONObject(i);

                UserModel user = Application.getInstance().getUsersManager().get(userJson);
                if (user == null) {
                    user = Application.getInstance().getUsersManager().add(userJson);
                }

                users.add(user);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return users;
    }
}
