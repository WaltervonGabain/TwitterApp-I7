package nl.saxion.act.i7.quitter.tasks;

public abstract class AsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result> {
    private TaskResponse<Result> delegate = null;

    public AsyncTask() {
    }

    public AsyncTask(TaskResponse<Result> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (this.delegate != null) {
            this.delegate.onResponse(result);

        }
    }
}
