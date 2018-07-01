package nl.saxion.act.i7.quitter.managers;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * The manager for preferences.
 */
public class SharedPreferencesManager {
    /***
     * The instance of the class.
     */
    private static SharedPreferencesManager instance;

    /***
     * A reference to the shared preferences.
     */
    private SharedPreferences preferences;

    /***
     * A reference to the shared preferences editor.
     */
    private SharedPreferences.Editor shEditor;

    /***
     * Construct the class with arguments.
     *
     * @param context The context of the application.
     */
    public static void construct(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
    }

    /***
     * Get the instance of the class, throws an exception if {@link #construct} isn't called first.
     *
     * @return The instance of the class.
     */
    public static SharedPreferencesManager getInstance() {
        if(instance == null) {
            throw new RuntimeException("Use construct(Context) at least once before using this method");
        }

        return instance;
    }

    /***
     * The class constructor.
     *
     * @param context The context of the application.
     */
    private SharedPreferencesManager(Context context) {
        this.preferences = context.getSharedPreferences("quitter", Context.MODE_PRIVATE);
    }

    /**
     * Save the changes.
     */
    public void apply() {
        if (this.shEditor != null) {
            this.shEditor.apply();
            this.shEditor = null;
        }
    }

    /***
     * Clear the preferences.
     */
    public void clear() {
        this.edit();
        this.shEditor.clear();
        this.apply();
    }

    /***
     * Get a string from the preferences.
     *
     * @param key The key of the string.
     * @param defaultValue The default value.
     *
     * @return The value of the key or default value if it does't exists.
     */
    public String getString(String key, String defaultValue) {
        return this.preferences.getString(key, defaultValue);
    }

    /***
     * Put a new item in the preferences.
     *
     * @param key The key of the item.
     * @param value The Value of the item.
     */
    public void putString(String key, String value) {
        this.edit();
        this.shEditor.putString(key, value);
    }

    /***
     * Create a new instance of the editor if it doesn't exist.
     */
    private void edit() {
        if (this.shEditor == null) {
            this.shEditor = this.preferences.edit();
        }
    }
}
