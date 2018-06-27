package nl.saxion.act.i7.quitter;

import android.os.UserManager;

import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.managers.UsersManager;

public class Application extends android.app.Application {
    private static Application instance;

    private UsersManager usersManager = new UsersManager();

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        SharedPreferencesManager.construct(this);
    }

    public UsersManager getUsersManager() {
        return this.usersManager;
    }
}
