package nl.saxion.act.i7.quitter.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private long id;

    private String name;

    private String username;

    private String description;

    public UserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.name = jsonObject.getString("name");
        this.username = jsonObject.getString("screen_name");
        this.description = jsonObject.getString("description");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }
}
