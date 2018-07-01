package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.activities.MainActivity;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterUpdateStatusTask;

/***
 * The fragment that let the user post a tweet.
 */
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
                button.setEnabled(false);

                EditText editText = view.findViewById(R.id.etText);
                String text = editText.getText().toString();

                if (!text.isEmpty()) {
                    editText.clearFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    new TwitterUpdateStatusTask(text)
                            .onError(() -> {
                                button.setEnabled(true);
                                Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            })
                            .onResult((tweet) -> {
                                button.setEnabled(true);

                                if (tweet != null) {
                                    Snackbar.make(view, R.string.tweeted, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    editText.setText("");

                                    // We posted something, let's reload since we know that this parent is MainActivity.
                                    MainActivity context = (MainActivity) this.getContext();
                                    if (context != null) {
                                        context.reloadTimeline();
                                    }
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
