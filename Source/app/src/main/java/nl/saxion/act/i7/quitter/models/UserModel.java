package nl.saxion.act.i7.quitter.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

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
     * The screen name of the user.
     */
    private String screenName;

    /***
     * The description of the user.
     */
    private String description;

    private Bitmap backgroundImage;

    private Bitmap profileImage;

    private Integer profileTextColor;

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
            this.screenName = String.format("@%s", jsonObject.getString("screen_name"));
            this.description = jsonObject.getString("description");

            // This is called from AsyncThread in "doInBackground" so we don't need to use another async operation here.
            String bannerUrl = jsonObject.getString("profile_banner_url");
            if (bannerUrl != null && !bannerUrl.equals("null")) {
                backgroundImage = Picasso.get().load(bannerUrl).get();
                profileTextColor = Color.parseColor(String.format("#%s", jsonObject.getString("profile_text_color")));
            }

            profileImage = Picasso.get()
                    .load(jsonObject.getString("profile_image_url").replace("_normal", ""))
                    .transform(new CircleTransform())
                    .get();

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
     * Get the screen name of the user.
     *
     * @return The screen name of the user.
     */
    public String getScreenName() {
        return screenName;
    }

    /***
     * Get the description of the user.
     *
     * @return The description of the user.
     */
    public String getDescription() {
        return description;
    }

    public Bitmap getBackgroundImage() {
        return this.backgroundImage;
    }

    public Bitmap getProfileImage() {
        return this.profileImage;
    }

    public Integer getProfileTextColor() {
        return this.profileTextColor;
    }
}
