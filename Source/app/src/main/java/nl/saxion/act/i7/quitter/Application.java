package nl.saxion.act.i7.quitter;

import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.managers.UsersManager;

/***
 * The base class of the application.
 */
public class Application extends android.app.Application {
    /***
     * The instance.
     */
    private static Application instance;

    /***
     * The user manager instance.
     */
    private UsersManager usersManager = new UsersManager();

    /***
     * Get the instance of the class.
     *
     * @return The instance of this class.
     */
    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        SharedPreferencesManager.construct(this);
    }

    /***
     * Get the users managers.
     *
     * @return {@link UsersManager} reference.
     */
    public UsersManager getUsersManager() {
        return this.usersManager;
    }
}
