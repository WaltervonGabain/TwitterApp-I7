package nl.saxion.act.i7.quitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.AuthorizationManager;

public class LoginActivity extends AppCompatActivity {
    public void onLoginClick(View view) {

        AuthorizationManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}

