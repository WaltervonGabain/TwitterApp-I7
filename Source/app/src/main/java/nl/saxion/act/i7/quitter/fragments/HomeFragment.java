package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterHomeTimelineTask;

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
            new TwitterHomeTimelineTask()
                    .onResult((tweets) -> {
                        ListView listView = view.findViewById(R.id.lvTweets);
                        listView.setAdapter(new TweetDataAdapter(context, tweets));
                    })
                    .execute();
        }
    }
}
