package nl.saxion.act.i7.quitter.managers;

import android.util.Log;
import android.util.LongSparseArray;

import org.json.JSONObject;

import nl.saxion.act.i7.quitter.models.UserModel;

/***
 * A class that keep track of users.
 */
public class UsersManager {
    /***
     * Current logged in user.
     */
    private UserModel currentUser;

    /***
     * Users that have occurred in the life cycle of the application.
     */
    private LongSparseArray<UserModel> cachedUsers = new LongSparseArray<>();

    /***
     * The constructor.
     */
    public UsersManager() {
    }

    /***
     * Add a new user to the list.
     *
     * @param user The user model that needs to be added.
     */
    public void add(UserModel user) {
        this.cachedUsers.put(user.getId(), user);
    }

    /***
     * Create a new {@link UserModel}, add it to the list and return it.
     *
     * @param jsonObject The JSON object of the user.
     *
     * @return The user that has been added.
     */
    public UserModel add(JSONObject jsonObject) {
        UserModel user = new UserModel(jsonObject);
        this.cachedUsers.put(user.getId(), user);

        return user;
    }

    public void clear() {
        this.cachedUsers.clear();
    }

    /***
     * Get an user by ID.
     *
     * @param id The user's ID.
     *
     * @return The user object with that ID.
     */
    public UserModel get(long id) {
        return this.cachedUsers.get(id);
    }

    /***
     * Get an user by his JSON object.
     *
     * @param jsonObject The JSON object of the user.
     *
     * @return The user that correspond with the ID in the JSON object.
     */
    public UserModel get(JSONObject jsonObject) {
        try {
            return this.cachedUsers.get(jsonObject.getLong("id"));
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    /***
     * Remove an user from the list.
     *
     * @param id The ID of the user.
     */
    public void remove(long id) {
        this.cachedUsers.remove(id);
    }

    /***
     * Get the current logged in user.
     *
     * @return The logged in user.
     */
    public UserModel getCurrentUser() {
        return currentUser;
    }

    /***
     * Set the current logged in user.
     *
     * @param currentUser The new logged in user.
     */
    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }
}
