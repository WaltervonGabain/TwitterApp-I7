package nl.saxion.act.i7.quitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.saxion.act.i7.quitter.activities.AuthorizationActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent = new Intent(this, AuthorizationActivity.class);
        this.startActivity(activityIntent);
        this.finish();
    }
}
