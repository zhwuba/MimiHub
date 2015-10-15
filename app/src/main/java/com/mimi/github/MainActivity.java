package com.mimi.github;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mimi.github.Util.AvatarLoader;
import com.mimi.github.Util.PreferenceUtils;
import com.mimi.github.core.user.UserComparator;
import com.mimi.github.persistence.AccountDataManager;
import com.mimi.github.ui.TabPagerActivity;
import com.mimi.github.ui.repo.OrganizationLoader;
import com.mimi.github.ui.user.HomePagerAdapter;

import org.eclipse.egit.github.core.User;

import java.util.Collections;
import java.util.List;

public class MainActivity extends TabPagerActivity<HomePagerAdapter>
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<User>> {

    private final static String TAG = "mimi.MainActivity";

    private static final String PREF_ORG_ID = "orgId";

    private List<User> orgs = Collections.emptyList();

    private User org;

    @Inject
    private AccountDataManager accountDataManager;

    @Inject
    private Provider<UserComparator> userComparatorProvider;

    @Inject
    private AvatarLoader avatars;

    @Inject
    private SharedPreferences sharedPreferences;

    private boolean isDefaultUser;

    @Override
    protected HomePagerAdapter createAdapter() {
        return new HomePagerAdapter(this, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected int getContentView(){
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG," -------------- onCreateLoader---------");
        return new OrganizationLoader(this,accountDataManager,userComparatorProvider);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> orgs) {
        Log.d(TAG,"--------  onLoadFinished ----- = " + orgs);
        this.orgs = orgs;

        int sharedPreferencesOrgId = sharedPreferences.getInt(PREF_ORG_ID, -1);
        int targetOrgId = org == null ? sharedPreferencesOrgId : org.getId();

       /* Menu menu = navigationView.getMenu();
        menu.removeGroup(R.id.user_select);
        for (int i = 0; i < orgs.size(); ++i) {
            final MenuItem item = menu.add(R.id.user_select, i, Menu.NONE, orgs.get(i).getLogin());
            avatars.bind(item, orgs.get(i));
            if (orgs.get(i).getId() == targetOrgId) {
                setOrg(orgs.get(i));
            }
        }*/

        // If the target org is invalid (e.g. first login), select the first one
        if (targetOrgId == -1 && orgs.size() > 0) {
            setOrg(orgs.get(0));
        }

        //menu.setGroupVisible(R.id.user_select, false);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }

    private void setOrg(User org) {
        Log.d(TAG, "setOrg : " + org.getLogin());

        PreferenceUtils.save(sharedPreferences.edit().putInt(PREF_ORG_ID,
                org.getId()));

        // Don't notify listeners or change pager if org hasn't changed
        if (this.org != null && this.org.getId() == org.getId())
            return;

        this.org = org;

        avatars.bind((ImageView) findViewById(R.id.imageView), org);
        ((TextView) findViewById(R.id.textView)).setText(org.getLogin());
    }
}
