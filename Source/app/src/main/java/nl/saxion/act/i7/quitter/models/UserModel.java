package nl.saxion.act.i7.quitter.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import nl.saxion.act.i7.quitter.utilities.CircleTransform;

/***
 * A model for an user.
 */
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

    /***
     * The background image of the user.
     */
    private Bitmap backgroundImage;

    /***
     * The profile picture (48x48) image of the user.
     */
    private Bitmap normalProfileImage;

    /***
     * The profile picture (73x73) image of the user.
     */
    private Bitmap biggerProfileImage;

    /***
     * The profile picture (original size) image of the user.
     */
    private Bitmap hugeProfileImage;

    /***
     * The text color on the user's profile.
     */
    private Integer profileTextColor = Color.rgb(0, 0, 0);

    /***
     * A boolean that is true if the current logged in user follow this user, false otherwise.
     */
    private boolean followed = false;

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
            try {
                String bannerUrl = jsonObject.getString("profile_banner_url");
                if (bannerUrl != null && !bannerUrl.equals("null")) {
                    this.backgroundImage = Picasso.get().load(bannerUrl).get();
                    this.profileTextColor = Color.parseColor(String.format("#%s", jsonObject.getString("profile_text_color")));
                }
            } catch (Exception ex) {
            }

            String imageUrl = jsonObject.getString("profile_image_url");

            this.normalProfileImage = Picasso.get()
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .get();

            this.biggerProfileImage = Picasso.get()
                    .load(imageUrl.replace("_normal", "_bigger"))
                    .transform(new CircleTransform())
                    .get();

            this.hugeProfileImage = Picasso.get()
                    .load(imageUrl.replace("_normal", "_400x400"))
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
        return this.id;
    }

    /***
     * Get the name of the user.
     *
     * @return The name the user.
     */
    public String getName() {
        return this.name;
    }

    /***
     * Get the screen name of the user.
     *
     * @return The screen name of the user.
     */
    public String getScreenName() {
        return this.screenName;
    }

    /***
     * Get the description of the user.
     *
     * @return The description of the user.
     */
    public String getDescription() {
        return this.description;
    }

    /***
     * Get the background image of the user.
     *
     * @return The background image of the user.
     */
    public Bitmap getBackgroundImage() {
        return this.backgroundImage;
    }

    /***
     * Get the profile picture (48x48) image of the user.
     *
     * @return The profile picture (48x48) image of the user.
     */
    public Bitmap getNormalProfileImage() {
        return this.normalProfileImage;
    }

    /***
     * Get the profile picture (73x73) image of the user.
     *
     * @return The profile picture (73x73) image of the user.
     */
    public Bitmap getBiggerProfileImage() {
        return this.biggerProfileImage;
    }

    /***
     * Get the profile picture (original size) image of the user.
     *
     * @return The profile picture (original size) image of the user.
     */
    public Bitmap getHugeProfileImage() {
        return this.hugeProfileImage;
    }

    /***
     * The text color on the user's profile.
     *
     * @return The text color on the user's profile.
     */
    public Integer getProfileTextColor() {
        return this.profileTextColor;
    }

    /***
     * Get the followed flag.
     *
     * @return True if the user is followed by the current logged in user, false otherwise.
     */
    public boolean isFollowed() {
        return this.followed;
    }

    /***
     * Set the followed status.
     *
     * @param followed True if this user should be marked as followed, false otherwise.
     */
    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
