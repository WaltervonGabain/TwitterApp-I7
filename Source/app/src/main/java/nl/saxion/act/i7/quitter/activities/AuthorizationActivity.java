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

public class AuthorizationActivity extends AppCompatActivity {
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

    private void onAuthorizationUrl(String authorizationUrl) {
        WebView webView = this.findViewById(R.id.webView);
        RelativeLayout relativeLayout = this.findViewById(R.id.pleaseWaitLayout);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (authorizationUrl.equals(url)) {
                    webView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().startsWith(AuthManager.CALLBACK_URL)) {
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
