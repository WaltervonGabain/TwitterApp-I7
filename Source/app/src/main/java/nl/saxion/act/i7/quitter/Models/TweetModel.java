package nl.saxion.act.i7.quitter.Models;

import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.act.i7.quitter.DataProvider.UserDataProvider;

public class TweetModel {
    private String text;

    private UserModel user;

    public TweetModel(JSONObject jsonObject) throws JSONException {
        this.text = jsonObject.getString("text");
        this.user = UserDataProvider.getOrAdd(jsonObject.getJSONObject("user"));
    }

    public String getText() {
        return text;
    }

    public UserModel getUser() {
        return user;
    }
}
