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
import nl.saxion.act.i7.quitter.data_adapters.ProfileDataAdapter;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterFriendsListTask;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterFriendshipLookupTask;

/***
 * The fragment that shows what the user is following.
 */
public class FollowingFragment extends Fragment {
    public FollowingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = this.getContext();
        View view = this.getView();

        if (context != null && view != null) {
            new TwitterFriendsListTask()
                    .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                    .onResult((users) -> {
                        View pleaseWait = view.findViewById(R.id.cvPleaseWait);

                        if (users.isEmpty()) {
                            View nothingToShow = view.findViewById(R.id.cvNothingToShow);
                            nothingToShow.setVisibility(View.VISIBLE);

                            pleaseWait.setVisibility(View.GONE);
                        } else {
                            // Now check for the friendship of the users and our user.
                            new TwitterFriendshipLookupTask(users)
                                    .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                                    .onResult((result) -> {
                                        ListView listView = view.findViewById(R.id.lvProfiles);
                                        listView.setAdapter(new ProfileDataAdapter(context, users));

                                        listView.setVisibility(View.VISIBLE);
                                        pleaseWait.setVisibility(View.GONE);
                                    })
                                    .execute();
                        }
                    })
                    .execute();
        }
    }
}
