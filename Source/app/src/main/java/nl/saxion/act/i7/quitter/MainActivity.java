package nl.saxion.act.i7.quitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.InputStream;

import nl.saxion.act.i7.quitter.DataAdapter.TweetDataAdapter;
import nl.saxion.act.i7.quitter.DataProvider.TweetDataProvider;

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
    }
}
