package nl.saxion.act.i7.quitter.models;

import android.util.Log;

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

    private String profileImageUrl;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON object for the user.
     */
    public UserModel(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.name = jsonObject.getString("name");
            this.username = String.format("@%s", jsonObject.getString("screen_name"));
            this.description = jsonObject.getString("description");

            this.profileImageUrl = jsonObject.getString("profile_image_url_https");

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
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

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }
}
