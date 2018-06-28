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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import nl.saxion.act.i7.quitter.Application;
import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.fragments.HomeFragment;
import nl.saxion.act.i7.quitter.fragments.ProfileFragment;
import nl.saxion.act.i7.quitter.fragments.SearchFragment;
import nl.saxion.act.i7.quitter.managers.AuthManager;
import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.managers.UsersManager;
import nl.saxion.act.i7.quitter.models.UserModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = this.findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);

        SearchView searchView = this.findViewById(R.id.searchView);
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_content);
                if(!(fragment instanceof SearchFragment)) {
                    this.loadFragment(SearchFragment.class, null);
                }
            }
        });

        this.bindUserDetails();

        if (savedInstanceState == null) {
            this.loadFragment(HomeFragment.class, null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            this.setActiveItem(fragmentManager.findFragmentById(R.id.fragment_content));
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        this.setActiveItem(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            this.loadFragment(HomeFragment.class, null);
        }
        else if (id == R.id.nav_my_profile) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", Application.getInstance().getUsersManager().getCurrentUser().getId());

            this.loadFragment(ProfileFragment.class, bundle);
        }
        else if (id == R.id.nav_search) {
            this.loadFragment(SearchFragment.class, null);
        }
        else if (id == R.id.nav_logout) {
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

    public void loadFragment(Class fragmentClass, Bundle bundle) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            if (bundle != null) {
                fragment.setArguments(bundle);
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), ex.getLocalizedMessage(), ex);
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment, fragmentClass.getName()).addToBackStack(null).commit();
    }

    private void bindUserDetails() {
        UserModel currentUser = Application.getInstance().getUsersManager().getCurrentUser();

        View headerView = this.navigationView.getHeaderView(0);

        TextView tvName = headerView.findViewById(R.id.tvName);
        tvName.setText(currentUser.getName());
        tvName.setTextColor(currentUser.getProfileTextColor());

        TextView tvUsername = headerView.findViewById(R.id.tvUsername);
        tvUsername.setText(currentUser.getScreenName());
        tvUsername.setTextColor(currentUser.getProfileTextColor());

        headerView.setBackground(new BitmapDrawable(this.getBaseContext().getResources(), currentUser.getBackgroundImage()));

        ImageView imageView = headerView.findViewById(R.id.ivProfileImage);
        imageView.setImageBitmap(currentUser.getBiggerProfileImage());
    }

    private void setActiveItem(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            this.navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        } else if (fragment instanceof ProfileFragment) {
            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                UsersManager usersManager = Application.getInstance().getUsersManager();
                long userId = bundle.getLong("id");

                if (userId == usersManager.getCurrentUser().getId()) {
                    this.navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(true);
                } else {
                    this.navigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                }
            }
        } else if (fragment instanceof SearchFragment) {
            this.navigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
        }
    }
}
