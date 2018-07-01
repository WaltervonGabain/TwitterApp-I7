package nl.saxion.act.i7.quitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.UsersManager;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterFriendshipLookupTask;
import nl.saxion.act.i7.quitter.utilities.ProfileUtil;

/***
 * The fragment that show the profile of a user.
 */
public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = this.getContext();
        View view = this.getView();

        if (context != null && view != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                UsersManager usersManager = Application.getInstance().getUsersManager();
                long userId = bundle.getLong("id");

                UserModel user;
                if (userId == usersManager.getCurrentUser().getId()) {
                    user = usersManager.getCurrentUser();
                    ProfileUtil.bind(user, context, view);
                } else {
                    user = usersManager.get(userId);

                    // Check the friendship status.
                    new TwitterFriendshipLookupTask(user)
                            .onError(() -> Snackbar.make(view, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).setAction("Action", null).show())
                            .onResult((result) -> ProfileUtil.bind(user, context, view))
                            .execute();
                }

                Bundle timelineBundle = new Bundle();
                timelineBundle.putLong("id", user.getId());

                Fragment fragment = this.getChildFragmentManager().findFragmentById(R.id.fragmentTimeline);
                fragment.setArguments(timelineBundle);
            }
        }
    }
}
