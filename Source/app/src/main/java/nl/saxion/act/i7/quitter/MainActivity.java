package nl.saxion.act.i7.quitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.InputStream;

import nl.saxion.act.i7.quitter.activities.UserDetailedActivity;
import nl.saxion.act.i7.quitter.data_adapters.TweetDataAdapter;
import nl.saxion.act.i7.quitter.data_providers.TweetDataProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream stream = this.getBaseContext().getResources().openRawResource(R.raw.tweets);

        try {
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);

            String fileContent = new String(bytes);
            TweetDataProvider.reload(new JSONObject(fileContent));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TweetDataAdapter tweetDataAdapter = new TweetDataAdapter(this, TweetDataProvider.getAll());

        ListView tweetList = this.findViewById(R.id.tweetList);
        tweetList.setAdapter(tweetDataAdapter);

        tweetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, UserDetailedActivity.class);
                intent.putExtra("index", position);

                startActivity(intent);
            }
        });
    }
}
