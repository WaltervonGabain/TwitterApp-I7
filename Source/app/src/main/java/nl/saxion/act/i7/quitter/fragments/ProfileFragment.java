package nl.saxion.act.i7.quitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.UsersManager;
import nl.saxion.act.i7.quitter.models.UserModel;

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

        View view = this.getView();
        if (view != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                UsersManager usersManager = Application.getInstance().getUsersManager();
                long userId = bundle.getLong("id");

                UserModel user;
                if (userId == usersManager.getCurrentUser().getId()) {
                    user = usersManager.getCurrentUser();
                } else {
                    user = usersManager.get(userId);
                }

                ImageView imageView = view.findViewById(R.id.ivBackgroundImage);
                imageView.setImageBitmap(user.getBackgroundImage());

                imageView = view.findViewById(R.id.ivProfileImage);
                imageView.setImageBitmap(user.getHugeProfileImage());

                TextView textView = view.findViewById(R.id.tvName);
                textView.setText(user.getName());

                textView = view.findViewById(R.id.tvScreenName);
                textView.setText(user.getScreenName());

                textView = view.findViewById(R.id.tvDescription);

                if (user.getDescription().isEmpty()) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setText(user.getDescription());
                }

                Bundle timelineBundle = new Bundle();
                timelineBundle.putLong("id", user.getId());

                Fragment fragment = this.getChildFragmentManager().findFragmentById(R.id.fragmentTimeline);
                fragment.setArguments(timelineBundle);
            }
        }
    }
}
