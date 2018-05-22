package nl.saxion.act.i7.quitter.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private long id;

    private String name;

    public UserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.name = jsonObject.getString("name");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
