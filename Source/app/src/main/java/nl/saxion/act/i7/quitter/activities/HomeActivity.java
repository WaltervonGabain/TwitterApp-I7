package nl.saxion.act.i7.quitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.InputStream;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.data_providers.TweetDataProvider;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
