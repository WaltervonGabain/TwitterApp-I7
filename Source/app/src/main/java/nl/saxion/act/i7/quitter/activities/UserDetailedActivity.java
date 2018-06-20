package nl.saxion.act.i7.quitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.data_providers.TweetDataProvider;
import nl.saxion.act.i7.quitter.model.UserModel;

public class UserDetailedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detailed);

        int index = this.getIntent().getIntExtra("index", 0);
        UserModel userModel = TweetDataProvider.get(index).getUser();

        this.setTitle(String.format(this.getBaseContext().getString(R.string.user_profile_title), userModel.getUsername()));

        TextView textView = this.findViewById(R.id.nameText);
        textView.setText(userModel.getName());

        textView = this.findViewById(R.id.usernameText);
        textView.setText(String.format("@%s", userModel.getUsername()));

        textView = this.findViewById(R.id.descriptionText);
        textView.setText(userModel.getDescription());
    }
}
