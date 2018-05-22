package nl.saxion.act.i7.quitter.DataAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.saxion.act.i7.quitter.Models.TweetModel;
import nl.saxion.act.i7.quitter.R;

public class TweetDataAdapter extends ArrayAdapter<TweetModel> {
    public TweetDataAdapter(@NonNull Context context, @NonNull ArrayList<TweetModel> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TweetModel tweetModel = this.getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_layout, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tweetText);
        textView.setText(tweetModel.getText());

        textView = convertView.findViewById(R.id.tweetUser);
        textView.setText(tweetModel.getUser().getName());

        return convertView;
    }
}
