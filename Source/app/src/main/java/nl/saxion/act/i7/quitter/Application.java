package nl.saxion.act.i7.quitter;

import io.reactivex.subjects.BehaviorSubject;
import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.models.UserModel;

public class Application extends android.app.Application {
    private static Application instance;

    private BehaviorSubject<UserModel> currentUser;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        this.currentUser = BehaviorSubject.create();

        SharedPreferencesManager.construct(this);
    }

    public UserModel getCurrentUser() {
        return this.currentUser.getValue();
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser.onNext(currentUser);
    }
}
