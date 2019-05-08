package com.uday.finitiativesassignment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.fragments.CoinsFragment;
import com.uday.finitiativesassignment.fragments.EventsFragment;
import com.uday.finitiativesassignment.fragments.WatchersFragment;
import com.uday.finitiativesassignment.utilites.Utility;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_coins:
                    txtTitle.setText(getResources().getString(R.string.coins));
                    Utility.navigateDashBoardFragment(new CoinsFragment(), CoinsFragment.TAG, null, MainActivity.this);

                    return true;
                case R.id.nav_watchers:
                    txtTitle.setText(getResources().getString(R.string.watchers));
                    Utility.navigateDashBoardFragment(new WatchersFragment(), WatchersFragment.TAG, null, MainActivity.this);

                    return true;
                case R.id.nav_events:
                    txtTitle.setText(getResources().getString(R.string.events));
                    Utility.navigateDashBoardFragment(new EventsFragment(), EventsFragment.TAG, null, MainActivity.this);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTitle = findViewById(R.id.txtTitle);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        txtTitle.setText(getResources().getString(R.string.coins));
        Utility.navigateDashBoardFragment(new CoinsFragment(), CoinsFragment.TAG, null, this);

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager()
                    .getBackStackEntryAt(
                            getSupportFragmentManager()
                                    .getBackStackEntryCount() - 1);
            String tagName = backEntry.getName();
            if (tagName.equals(CoinsFragment.TAG)) {
                finishAffinity();
            }
        }
    }

}
