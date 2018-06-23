package nl.saxion.act.i7.quitter.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.fragments.HomeFragment;
import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.models.UserModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.bindUserDetails();

        if (savedInstanceState == null) {
            this.loadFragment(HomeFragment.class);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            AuthManager.getInstance().logout();
            SharedPreferencesManager.getInstance().clear();

            Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
            this.startActivity(intent);
            this.finish();
        }

        item.setChecked(true);

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void bindUserDetails() {
        UserModel currentUser = Application.getInstance().getCurrentUser();

        Disposable disposable;

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView tvName = headerView.findViewById(R.id.tvName);
        tvName.setText(currentUser.getName());

        TextView tvUsername = headerView.findViewById(R.id.tvUsername);
        tvUsername.setText(currentUser.getScreenName());

        disposable = currentUser.getProfileTextColorObservable().subscribe((color) -> {
            tvName.setTextColor(color);
            tvUsername.setTextColor(color);
        });
        compositeDisposable.add(disposable);

        disposable = currentUser.getBackgroundImageObservable().subscribe((image) -> {
            headerView.setBackground(new BitmapDrawable(this.getBaseContext().getResources(), image));
        });
        compositeDisposable.add(disposable);

        ImageView imageView = headerView.findViewById(R.id.ivProfileImage);

        disposable = currentUser.getProfileImageObservable().subscribe(imageView::setImageBitmap);
        compositeDisposable.add(disposable);
    }

    private void loadFragment(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();
    }
}