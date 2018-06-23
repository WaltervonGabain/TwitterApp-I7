package nl.saxion.act.i7.quitter.data_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.models.TweetModel;
import nl.saxion.act.i7.quitter.models.UserModel;

public class TweetDataAdapter extends ArrayAdapter<TweetModel> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TweetDataAdapter(@NonNull Context context, @NonNull ArrayList<TweetModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public void clear() {
        super.clear();

        this.compositeDisposable.dispose();
        this.compositeDisposable.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TweetModel tweet = this.getItem(position);
        UserModel user = tweet.getUser();

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.ivProfileImage);

        Disposable disposable = user.getProfileImageObservable().subscribe(imageView::setImageBitmap);
        this.compositeDisposable.add(disposable);

        TextView textView = convertView.findViewById(R.id.tvUserName);
        textView.setText(tweet.getUser().getName());

        textView = convertView.findViewById(R.id.tvScreenName);
        textView.setText(user.getScreenName());

        long dateMillis = tweet.getCreatedAt().getTime();
        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        textView = convertView.findViewById(R.id.tvTime);
        textView.setText(relativeDate);

        textView = convertView.findViewById(R.id.tvText);
        textView.setText(tweet.getText());

        return convertView;
    }
}
