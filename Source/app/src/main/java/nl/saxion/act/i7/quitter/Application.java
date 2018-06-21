package nl.saxion.act.i7.quitter;

import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesManager.construct(this);
    }
}
