package nl.saxion.act.i7.quitter.tasks;

import nl.saxion.act.i7.quitter.managers.AuthorizationManager;

public class AccessTokenExchangeTask extends AsyncTask<Void, Void, Void> {
    public AccessTokenExchangeTask(TaskResponse<Void> delegate) {
        super(delegate);
    }

    @Override
    protected Void doInBackground(Void... parameters) {
        AuthorizationManager.getInstance().setAccessToken();
        return null;
    }
}
