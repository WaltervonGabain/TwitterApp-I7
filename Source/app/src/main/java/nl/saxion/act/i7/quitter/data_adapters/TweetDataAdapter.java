package nl.saxion.act.i7.quitter.data_adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.activities.MainActivity;
import nl.saxion.act.i7.quitter.fragments.ProfileFragment;
import nl.saxion.act.i7.quitter.models.TweetModel;
import nl.saxion.act.i7.quitter.models.UserModel;

public class TweetDataAdapter extends ArrayAdapter<TweetModel> {
    private boolean isProfilePage;

    public TweetDataAdapter(@NonNull Context context, @NonNull ArrayList<TweetModel> objects, boolean isProfilePage) {
        super(context, 0, objects);
        this.isProfilePage = isProfilePage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tweet, parent, false);
        }

        TweetModel tweet = this.getItem(position);

        if(tweet != null) {
            UserModel user = tweet.getUser();

            if (user != null) {
                ImageView imageView = convertView.findViewById(R.id.ivProfileImage);
                imageView.setImageBitmap(user.getBiggerProfileImage());

                if(!this.isProfilePage) {
                    imageView.setOnClickListener((view) -> {
                        MainActivity context = (MainActivity) this.getContext();

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", user.getId());

                        context.loadFragment(ProfileFragment.class, bundle);
                    });
                }

                TextView textView = convertView.findViewById(R.id.tvUserName);
                textView.setText(tweet.getUser().getName());

                textView = convertView.findViewById(R.id.tvScreenName);
                textView.setText(user.getScreenName());

                long dateMillis = tweet.getCreatedAt().getTime();
                String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

                textView = convertView.findViewById(R.id.tvTime);
                textView.setText(relativeDate);

                textView = convertView.findViewById(R.id.tvText);
                textView.setText(tweet.getText());
            }
        }

        return convertView;
    }
}
