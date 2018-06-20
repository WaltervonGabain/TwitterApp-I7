package nl.saxion.act.i7.quitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    /***
     * The id of the user.
     */
    private long id;

    /***
     * The name of the user.
     */
    private String name;

    /***
     * The username of the user.
     */
    private String username;

    /***
     * The description of the user.
     */
    private String description;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON object for the user.
     *
     * @throws JSONException JSON exception.
     */
    public UserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.name = jsonObject.getString("name");
        this.username = jsonObject.getString("screen_name");
        this.description = jsonObject.getString("description");
    }

    /***
     * Get the id of the user.
     *
     * @return The id of the user.
     */
    public long getId() {
        return id;
    }

    /***
     * Get the name of the user.
     *
     * @return The name the user.
     */
    public String getName() {
        return name;
    }

    /***
     * Get the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /***
     * Get the description of the user.
     *
     * @return The description of the user.
     */
    public String getDescription() {
        return description;
    }
}
