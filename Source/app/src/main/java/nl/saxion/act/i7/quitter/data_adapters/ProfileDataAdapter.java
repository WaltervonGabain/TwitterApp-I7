package nl.saxion.act.i7.quitter.data_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.utilities.ProfileUtil;

/***
 * The array adapter for profiles.
 */
public class ProfileDataAdapter extends ArrayAdapter<UserModel> {
    public ProfileDataAdapter(@NonNull Context context, @NonNull ArrayList<UserModel> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_profile, parent, false);
        }

        UserModel user = this.getItem(position);
        if (user != null) {
            ProfileUtil.bind(user, this.getContext(), convertView);
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        // We don't want users to click on this item, so let's return false every time.
        return false;
    }
}
