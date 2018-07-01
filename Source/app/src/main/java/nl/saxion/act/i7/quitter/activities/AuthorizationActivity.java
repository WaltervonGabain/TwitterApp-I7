package nl.saxion.act.i7.quitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.AuthManager;

/***
 * Authorization activity, this is the main activity that is displayed to the user when the application starts.
 */
public class AuthorizationActivity extends AppCompatActivity {
    /***
     * A list with disposables to remove them in "onDestroy" function to prevent memory leaks.
     */
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        Disposable disposable;

        if (AuthManager.getInstance().hasAccessToken()) {
            disposable = AuthManager.getInstance().login(null).subscribe(this::onCredentialsVerified);
        } else {
            disposable = AuthManager.getInstance().getAuthorizationUrl().subscribe(this::onAuthorizationUrl);
        }

        this.compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        this.compositeDisposable.dispose();
        super.onDestroy();
    }

    /***
     * Load the authorization URL and display it to the user.
     *
     * @param authorizationUrl The authorization URL.
     */
    private void onAuthorizationUrl(String authorizationUrl) {
        RelativeLayout relativeLayout = this.findViewById(R.id.pleaseWaitLayout);

        if (authorizationUrl.isEmpty()) {
            View noInternet = this.findViewById(R.id.cvNoInternet);
            noInternet.setVisibility(View.VISIBLE);

            relativeLayout.setVisibility(View.GONE);
        } else {
            WebView webView = this.findViewById(R.id.webView);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // If the URL match with the authorization URL then show the web view.
                    if (authorizationUrl.equals(url)) {
                        webView.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    // If the Twitter redirect us to the callback URL then we are successfully logged in.
                    if (request.getUrl().toString().startsWith(AuthManager.getInstance().getCallbackUrl())) {
                        webView.setVisibility(View.GONE);
                        webView.stopLoading();
                        webView.loadUrl("about:blank");

                        relativeLayout.setVisibility(View.VISIBLE);

                        String oAuthVerifier = request.getUrl().getQueryParameter("oauth_verifier");
                        Disposable disposable = AuthManager.getInstance().login(oAuthVerifier).subscribe((success) -> onCredentialsVerified(success));

                        compositeDisposable.add(disposable);
                    }

                    return false;
                }
            });

            webView.loadUrl(authorizationUrl);
        }
    }

    /***
     * Start the main activity if the user is logged in successfully or show the authorization web view again.
     *
     * @param success The status of the authorization.
     */
    private void onCredentialsVerified(boolean success) {
        if (success) {
            Intent activityIntent = new Intent(AuthorizationActivity.this, MainActivity.class);
            startActivity(activityIntent);
            finish();
        } else {
            Disposable disposable = AuthManager.getInstance().getAuthorizationUrl().subscribe(this::onAuthorizationUrl);
            this.compositeDisposable.add(disposable);
        }
    }
}
