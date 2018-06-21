package nl.saxion.act.i7.quitter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.github.scribejava.core.model.OAuth1AccessToken;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.AuthorizationManager;
import nl.saxion.act.i7.quitter.tasks.auth.AccessTokenExchangeTask;
import nl.saxion.act.i7.quitter.tasks.auth.AuthorizationUrlTask;
import nl.saxion.act.i7.quitter.tasks.TaskResponse;
import nl.saxion.act.i7.quitter.tasks.twitter.TwitterVerifyCredentialsTask;

public class AuthorizationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        SharedPreferences sharedPreferences = this.getSharedPreferences("Quitter", MODE_PRIVATE);
        AuthorizationManager.getInstance().setAccessToken(sharedPreferences.getString("token", ""), sharedPreferences.getString("tokenSecret", ""), sharedPreferences.getString("rawResponse", ""));

        if(AuthorizationManager.getInstance().getAccessToken() == null) {
            AuthorizationUrlTask authorizationUrlTask = new AuthorizationUrlTask(new TaskResponse<String>() {
                @Override
                public void onResponse(String output) {
                    onAuthorizationUrlTaskFinish(output);
                }
            });
            authorizationUrlTask.execute();
        } else {
            this.verifyCredentials();
        }
    }

    private void onAccessTokenExchangeTaskFinish() {
        this.verifyCredentials();
    }

    private void onAuthorizationUrlTaskFinish(final String authorizationUrl) {
        final WebView webView = this.findViewById(R.id.webView);
        final RelativeLayout relativeLayout = this.findViewById(R.id.pleaseWaitLayout);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(authorizationUrl)) {
                    webView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().startsWith(AuthorizationManager.CALLBACK_URL)) {
                    webView.setVisibility(View.GONE);
                    webView.stopLoading();
                    webView.loadUrl("about:blank");

                    relativeLayout.setVisibility(View.VISIBLE);

                    AuthorizationManager.getInstance().setVerifier(request.getUrl().getQueryParameter("oauth_verifier"));

                    AccessTokenExchangeTask accessTokenExchangeTask = new AccessTokenExchangeTask(new TaskResponse<Void>() {
                        @Override
                        public void onResponse(Void output) {
                            onAccessTokenExchangeTaskFinish();
                        }
                    });
                    accessTokenExchangeTask.execute();
                }

                return false;
            }
        });

        webView.loadUrl(authorizationUrl);
    }

    private void verifyCredentials() {
        final SharedPreferences.Editor shEditor = this.getSharedPreferences("Quitter", MODE_PRIVATE).edit();

        TwitterVerifyCredentialsTask verifyCredentialsTask = new TwitterVerifyCredentialsTask(new TaskResponse<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                if(success) {
                    OAuth1AccessToken accessToken = AuthorizationManager.getInstance().getAccessToken();

                    shEditor.putString("token", accessToken.getToken());
                    shEditor.putString("tokenSecret", accessToken.getTokenSecret());
                    shEditor.putString("rawResponse", accessToken.getRawResponse());

                    shEditor.apply();

                    Intent activityIntent = new Intent(AuthorizationActivity.this, HomeActivity.class);
                    startActivity(activityIntent);
                    finish();
                } else {
                    // TODO: Handle the error.
                }
            }
        });
        verifyCredentialsTask.execute();
    }
}
