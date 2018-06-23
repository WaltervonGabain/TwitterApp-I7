package nl.saxion.act.i7.quitter.tasks;

import android.util.Log;

import io.reactivex.functions.Consumer;

public abstract class AsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result> {
    private Consumer<Result> onCancelConsumer = null;

    private Consumer<Result> onResultConsumer = null;

    public AsyncTask<Params, Progress, Result> onCancel(Consumer<Result> consumer) {
        this.onCancelConsumer = consumer;
        return this;
    }

    public AsyncTask<Params, Progress, Result> onResult(Consumer<Result> consumer) {
        this.onResultConsumer = consumer;
        return this;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        try {
            if (this.onCancelConsumer != null) {
                this.onCancelConsumer.accept(null);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);

        try {
            if (this.onCancelConsumer != null) {
                this.onCancelConsumer.accept(result);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        try {
            if (this.onResultConsumer != null) {
                this.onResultConsumer.accept(result);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }
    }
}
