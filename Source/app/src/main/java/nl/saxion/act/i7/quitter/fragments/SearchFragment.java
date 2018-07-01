package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.activities.MainActivity;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterUsersSearchTask;

/***
 * The search fragment.
 */
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

            ListView listView = view.findViewById(R.id.lvSearchResult);
            View pleaseWait = view.findViewById(R.id.cvPleaseWait);

            SearchView searchView = mainActivity.findViewById(R.id.searchView);

            if (!searchView.isFocused()) {
                searchView.requestFocusFromTouch();

                InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(searchView.findFocus(), 0);
                }
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    new TwitterUsersSearchTask(query)
                            .onResult((tweets) -> {
                                listView.setAdapter(new TweetDataAdapter(context, tweets, false));
                                listView.setVisibility(View.VISIBLE);

                                pleaseWait.setVisibility(View.GONE);
                            })
                            .execute();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    listView.setVisibility(View.GONE);
                    pleaseWait.setVisibility(View.VISIBLE);

                    return false;
                }
            });
        }
    }
}
