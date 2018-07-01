package nl.saxion.act.i7.quitter.tasks.twitter;

import android.util.Log;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.models.UserModel;

/***
 * The task to check the friendship between logged in user and a/between specific user(s).
 */
public class TwitterFriendshipLookupTask extends TwitterApiTask<Void> {
    /***
     * An array of users to join their id in the request.
     */
    private ArrayList<UserModel> users;

    /***
     * The constructor.
     *
     * @param user A single user to check.
     */
    public TwitterFriendshipLookupTask(UserModel user) {
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    /***
     * The constructor.
     *
     * @param users Multiple users to check.
     */
    public TwitterFriendshipLookupTask(ArrayList<UserModel> users) {
        this.users = users;
    }

    @Override
    String getEndpoint() {
        return "friendships/lookup.json";
    }

    @Override
    Verb getMethod() {
        return Verb.GET;
    }

    @Override
    void onPreRequestSign(OAuthRequest request) {
        StringBuilder ids = new StringBuilder();

        for (UserModel user : this.users) {
            ids.append(String.format("%s,", user.getId()));
        }

        // Remove the last comma.
        ids.deleteCharAt(ids.length() - 1);

        request.addParameter("user_id", ids.toString());
    }

    @Override
    Void onSuccess(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                long id = jsonObject.getLong("id");
                UserModel user = Application.getInstance().getUsersManager().get(id);

                JSONArray connections = jsonObject.getJSONArray("connections");
                for (int j = 0; j < connections.length(); j++) {
                    String connection = connections.getString(j);
                    if (connection.equals("following")) {
                        user.setFollowed(true);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }
}
