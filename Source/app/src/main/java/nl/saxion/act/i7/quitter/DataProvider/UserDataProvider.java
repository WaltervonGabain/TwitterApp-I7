package nl.saxion.act.i7.quitter.DataProvider;

import android.util.LongSparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.act.i7.quitter.Models.UserModel;

public class UserDataProvider {
    private static LongSparseArray<UserModel> users = new LongSparseArray<>();

    public static UserModel add(JSONObject jsonObject) throws JSONException {
        UserModel result = new UserModel(jsonObject);
        users.put(result.getId(), result);
        return result;
    }

    public static UserModel get(long id) {
        return users.get(id);
    }

    public static UserModel get(JSONObject jsonObject) throws JSONException {
        return users.get(jsonObject.getLong("id"));
    }

    public static UserModel getOrAdd(JSONObject jsonObject) throws JSONException {
        UserModel result = get(jsonObject);
        if(result == null) {
            result = add(jsonObject);
        }

        return result;
    }
}
