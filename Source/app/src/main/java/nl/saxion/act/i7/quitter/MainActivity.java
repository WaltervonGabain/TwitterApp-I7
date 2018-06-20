package nl.saxion.act.i7.quitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.saxion.act.i7.quitter.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent activityIntent;

        activityIntent = new Intent(this, LoginActivity.class);

        startActivity(activityIntent);
        finish();
    }
}
