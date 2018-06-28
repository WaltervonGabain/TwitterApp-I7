package nl.saxion.act.i7.quitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterUpdateStatusTask;

public class PostTweetFragment extends Fragment {
    public PostTweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_tweet, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = this.getView();
        if (view != null) {
            UserModel user = Application.getInstance().getUsersManager().getCurrentUser();

            ImageView imageView = view.findViewById(R.id.ivProfileImage);
            imageView.setImageBitmap(user.getNormalProfileImage());

            Button button = view.findViewById(R.id.btTweet);
            button.setOnClickListener((v) -> {
                EditText editText = view.findViewById(R.id.etText);
                String text = editText.getText().toString();

                if (!text.isEmpty()) {
                   new TwitterUpdateStatusTask(text)
                           .onResult((tweet) -> {
                               if (tweet != null) {
                                   Snackbar.make(view, R.string.tweeted, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                   editText.setText("");
                               } else {
                                   Snackbar.make(view, R.string.tweetError, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                               }
                           })
                            .execute();
                }
            });
        }
    }
}
