package nl.saxion.act.i7.quitter.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String SETTINGS_NAME = "quitter";

    private static SharedPreferencesManager instance;

    private SharedPreferences preferences;

    private SharedPreferences.Editor shEditor;

    public static void construct(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
    }

    public static SharedPreferencesManager getInstance() {
        if(instance == null) {
            throw new RuntimeException("Use construct(Context) at least once before using this method");
        }

        return instance;
    }

    private SharedPreferencesManager(Context context) {
        this.preferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public void apply() {
        if (this.shEditor != null) {
            this.shEditor.apply();
            this.shEditor = null;
        }
    }

    public void clear() {
        this.edit();
        this.shEditor.clear();
        this.apply();
    }

    public String getString(String key, String defaultValue) {
        return this.preferences.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        this.edit();
        this.shEditor.putString(key, value);
    }

    private void edit() {
        if (this.shEditor == null) {
            this.shEditor = this.preferences.edit();
        }
    }
}
