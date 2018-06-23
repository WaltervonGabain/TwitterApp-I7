package nl.saxion.act.i7.quitter.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import nl.saxion.act.i7.quitter.utilities.CircleTransform;

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

    private BehaviorSubject<Bitmap> backgroundImage;

    private Observable<Bitmap> backgroundImageObservable;

    private BehaviorSubject<Bitmap> profileImage;

    private Observable<Bitmap> profileImageObservable;

    private BehaviorSubject<Integer> profileTextColor;

    private Observable<Integer> profileTextColorObservable;

    /***
     * Constructor.
     *
     * @param jsonObject The JSON object for the user.
     */
    @WorkerThread
    public UserModel(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.name = jsonObject.getString("name");
            this.username = String.format("@%s", jsonObject.getString("screen_name"));
            this.description = jsonObject.getString("description");

            this.backgroundImage = BehaviorSubject.create();
            this.backgroundImageObservable = this.backgroundImage.hide();

            this.profileImage = BehaviorSubject.create();
            this.profileImageObservable = this.profileImage.hide();

            this.profileTextColor = BehaviorSubject.create();
            this.profileTextColorObservable = this.profileTextColor.hide();

            String bannerUrl = jsonObject.getString("profile_banner_url");
            if (bannerUrl != null && !bannerUrl.equals("null")) {
                backgroundImage.onNext(Picasso.get().load(bannerUrl).get());
                profileTextColor.onNext(Color.parseColor(String.format("#%s", jsonObject.getString("profile_text_color"))));
            }

            profileImage.onNext(Picasso.get()
                    .load(jsonObject.getString("profile_image_url").replace("_normal", ""))
                    .transform(new CircleTransform())
                    .get());

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

    public Observable<Bitmap> getBackgroundImageObservable() {
        return this.backgroundImageObservable;
    }

    public Observable<Bitmap> getProfileImageObservable() {
        return this.profileImageObservable;
    }

    public Observable<Integer> getProfileTextColorObservable() {
        return this.profileTextColorObservable;
    }
}
