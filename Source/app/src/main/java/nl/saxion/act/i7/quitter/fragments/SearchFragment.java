package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.activities.MainActivity;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterUsersSearchTask;

public class SearchFragment extends Fragment {
    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = this.getContext();
        View view = this.getView();

        if (context != null && view != null) {
            MainActivity mainActivity = (MainActivity) context;

            SearchView searchView = mainActivity.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    new TwitterUsersSearchTask(query)
                            .onResult((tweets) -> {
                                ListView listView = view.findViewById(R.id.lvSearchResult);
                                listView.setAdapter(new TweetDataAdapter(context, tweets, false));
                            })
                            .execute();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

    }
}
