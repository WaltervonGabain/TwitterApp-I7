package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.models.TweetModel;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterHomeTimelineTask;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterUserTimelineTask;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = this.getContext();
        View view = this.getView();

        if (context != null && view != null) {
            Bundle bundle = this.getArguments();

            Consumer<ArrayList<TweetModel>> consumer = (tweets) -> {
                ListView listView = view.findViewById(R.id.lvTweets);

                // If we have a bundle, it is the profile page.
                listView.setAdapter(new TweetDataAdapter(context, tweets, bundle != null));

                listView.setVisibility(View.VISIBLE);

                View pleaseWait = view.findViewById(R.id.cvPleaseWait);
                pleaseWait.setVisibility(View.GONE);
            };

            FragmentManager fragmentManager = this.getChildFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentPostTweet);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(bundle == null) {
                fragmentTransaction.show(fragment);

                new TwitterHomeTimelineTask()
                        .onResult(consumer)
                        .execute();
            } else {
                fragmentTransaction.hide(fragment);

                new TwitterUserTimelineTask(bundle.getLong("id"))
                        .onResult(consumer)
                        .execute();
            }

            fragmentTransaction.commit();

            // TODO: Show a message if there are no tweets.
        }
    }
}
