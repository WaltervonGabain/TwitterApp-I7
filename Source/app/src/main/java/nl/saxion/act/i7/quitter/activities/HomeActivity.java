package nl.saxion.act.i7.quitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.saxion.act.i7.quitter.R;
import nl.saxion.act.i7.quitter.managers.AuthorizationManager;
import nl.saxion.act.i7.quitter.managers.SharedPreferencesManager;
import nl.saxion.act.i7.quitter.models.UserModel;
import nl.saxion.act.i7.quitter.utilities.CircleTransform;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.bindUserDetails();
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
            AuthorizationManager.getInstance().logout();
            SharedPreferencesManager.getInstance().clear();

            Intent intent = new Intent(HomeActivity.this, AuthorizationActivity.class);
            this.startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void bindUserDetails() {
        UserModel userModel = AuthorizationManager.getInstance().getUserModel();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);

        ImageView imageView = headerView.findViewById(R.id.ivProfileImage);
        Picasso.get().load(userModel.getProfileImageUrl()).resize(72, 72).transform(new CircleTransform()).into(imageView);

        TextView textView = headerView.findViewById(R.id.tvName);
        textView.setText(userModel.getName());

        textView = headerView.findViewById(R.id.tvUsername);
        textView.setText(userModel.getUsername());
    }
}
