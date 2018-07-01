package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterMentionsTimelineTask;

/***
 * The fragment for mentions.
 */
public class MentionsFragment extends Fragment {
    public MentionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mentions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = this.getContext();
        View view = this.getView();

        if (context != null && view != null) {
            new TwitterMentionsTimelineTask()
                    .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                    .onResult((tweets) -> {
                        if (tweets.isEmpty()) {
                            View nothingToShow = view.findViewById(R.id.cvNothingToShow);
                            nothingToShow.setVisibility(View.VISIBLE);
                        } else {
                            ListView listView = view.findViewById(R.id.lvTweets);
                            listView.setAdapter(new TweetDataAdapter(context, tweets, false));
                            listView.setVisibility(View.VISIBLE);
                        }

                        View pleaseWait = view.findViewById(R.id.cvPleaseWait);
                        pleaseWait.setVisibility(View.GONE);
                    })
                    .execute();
        }
    }
}
