package nl.saxion.act.i7.quitter.tasks;

import android.util.Log;

import io.reactivex.functions.Consumer;

/***
 * The base type of our custom async task.
 *
 * @param <Params> The type of the parameters.
 * @param <Progress> The type of the progress.
 * @param <Result> The type of the result.
 */
public abstract class AsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result> {
    /***
     * The callback that should be called when the task is canceled.
     */
    private Consumer<Result> onCancelConsumer = null;

    /***
     * The callback that should be called when the task is completed.
     */
    private Consumer<Result> onResultConsumer = null;

    /***
     * Set the cancel callback.
     *
     * @param consumer The callback to be called.
     *
     * @return This instance.
     */
    public AsyncTask<Params, Progress, Result> onCancel(Consumer<Result> consumer) {
        this.onCancelConsumer = consumer;
        return this;
    }

    /***
     * Set the result callback.
     *
     * @param consumer The callback to be called.
     *
     * @return This instance.
     */
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
