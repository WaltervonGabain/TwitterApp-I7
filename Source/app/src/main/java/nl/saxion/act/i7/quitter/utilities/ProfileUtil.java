package nl.saxion.act.i7.quitter.utilities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterFriendshipCreateTask;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterFriendshipDestroyTask;

public class ProfileUtil {
    public static void bind(UserModel user, Context context, View view) {
        ImageView imageView = view.findViewById(R.id.ivBackgroundImage);
        imageView.setImageBitmap(user.getBackgroundImage());

        imageView = view.findViewById(R.id.ivProfileImage);
        imageView.setImageBitmap(user.getHugeProfileImage());

        TextView textView = view.findViewById(R.id.tvName);
        textView.setText(user.getName());

        textView = view.findViewById(R.id.tvScreenName);
        textView.setText(user.getScreenName());

        // Don't show the follow button for the logged in user.
        if (Application.getInstance().getUsersManager().getCurrentUser().getId() != user.getId()) {
            Button button = view.findViewById(R.id.btFollow);
            button.setVisibility(View.VISIBLE);

            Runnable runnable = () -> {
                if (user.isFollowed()) {
                    button.setText(R.string.following);
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                } else {
                    button.setText(R.string.follow);
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                }
            };

            // Set the initial status of the button.
            runnable.run();

            button.setOnClickListener((v) -> {
                if (user.isFollowed()) {
                    new TwitterFriendshipDestroyTask(user.getId())
                            .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                            .onResult((result) -> {
                                user.setFollowed(false);
                                runnable.run();
                            })
                            .execute();
                } else {
                    new TwitterFriendshipCreateTask(user.getId())
                            .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                            .onResult((result) -> {
                                user.setFollowed(true);
                                runnable.run();
                            })
                            .execute();
                }
            });
        }

        textView = view.findViewById(R.id.tvDescription);

        if (user.getDescription().isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(user.getDescription());
        }
    }
}
