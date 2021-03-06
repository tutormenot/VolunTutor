package com.example.voluntutor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * This class allows for the UI to run in compliance with the individual fragments and the bottom navigation bar as well as user activity
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This method allows the GUI to load individual fragments and switch between views
     * @param fragment desired fragment
     * @return boolean
     */
    private boolean loadFragment (Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    /**
     * This method sets the content views and controls if the make user page gets displayed
     * Also sets the bottom navigation listener to allow the user to navigate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_name), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        MakeUserFragment.setID(sharedPref.getString(getString(R.string.path), null));


        if(sharedPref.getBoolean(getString(R.string.inFireBase), false)) { loadFragment(new HomeFragment()); }
        else { loadFragment(new MakeUserFragment()); }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    /**
     * Preserves information for when the user pauses app activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_name), 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.path), MakeUserFragment.getID());
        editor.putBoolean(getString(R.string.inFireBase), MakeUserFragment.getID() != null);
        editor.commit();
    }


    /**
     * This method allows the bottom navigation bar to be accessed on each screen, and
     * allows for the chosen screens to be displayed
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    /**
                     * Prevents the user from moving on from the make user page if they did not finish entering info
                     */
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if(currentFragment instanceof MakeUserFragment) {
                        Toast.makeText(getBaseContext(), "Please enter your information first", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Fragment selectedFragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.nav_search:
                                selectedFragment = new SearchFragment();
                                break;
                            case R.id.nav_hours:
                                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_name), 0);
                                boolean isTutor = sharedPref.getBoolean(getString(R.string.isTutor), true);
                                if (isTutor) {
                                    selectedFragment = new HoursFragment();
                                } else {
                                    selectedFragment = new HoursFragmentStudent();
                                }
                                break;
                            case R.id.nav_settings:
                                SharedPreferences sharedPref2 = getSharedPreferences(getString(R.string.shared_pref_name), 0);
                                boolean isTutor2 = sharedPref2.getBoolean(getString(R.string.isTutor), true);
                                if (isTutor2) {
                                    selectedFragment = new SettingsFragment();
                                } else {
                                    selectedFragment = new SettingsFragmentStudent();
                                }
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };






}
