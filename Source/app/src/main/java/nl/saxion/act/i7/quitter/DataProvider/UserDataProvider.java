package nl.saxion.act.i7.quitter.DataProvider;

import android.util.LongSparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.act.i7.quitter.Models.UserModel;

public class UserDataProvider {
    private static LongSparseArray<UserModel> users = new LongSparseArray<>();

    /***
     * Add an user to the list.
     *
     * @param jsonObject User JSON object.
     *
     * @return The user that was added.
     *
     * @throws JSONException JSON exception.
     */
    public static UserModel add(JSONObject jsonObject) throws JSONException {
        UserModel result = new UserModel(jsonObject);
        users.put(result.getId(), result);
        return result;
    }

    /***
     * Get an user by index.
     *
     * @param index Index of the user.
     *
     * @return The user at index or null if not exists.
     */
    public static UserModel getByIndex(int index) {
        return users.valueAt(index);
    }

    /***
     * Get the user with ID in the JSON object.
     *
     * @param jsonObject The JSON object that contain "id" value.
     *
     * @return The user or null if not exists.
     *
     * @throws JSONException JSON exception.
     */
    public static UserModel get(JSONObject jsonObject) throws JSONException {
        return users.get(jsonObject.getLong("id"));
    }

    /***
     * Get the user with ID in the JSON object or add it if not exist.
     *
     * @param jsonObject The user JSON object.
     *
     * @return The user that was added.
     *
     * @throws JSONException JSON exception.
     */
    public static UserModel getOrAdd(JSONObject jsonObject) throws JSONException {
        UserModel result = get(jsonObject);
        if(result == null) {
            result = add(jsonObject);
        }

        return result;
    }
}
