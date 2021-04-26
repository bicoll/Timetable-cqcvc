package com.evercocer.educationhelper.activitys;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.fragments.MeFragment;
import com.evercocer.educationhelper.fragments.SearchFragment;
import com.evercocer.educationhelper.fragments.TimetableFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bNav;
    private FragmentManager fragmentManager;
    private Fragment timetableFragment;
    private Fragment searchFragment;
    private Fragment meFragment;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intViews();
        initDefaultFragment();
    }


    private void initDefaultFragment() {
        if (timetableFragment == null)
            timetableFragment = new TimetableFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_content, timetableFragment)
                .commit();
    }

    private void intViews() {
        bNav = findViewById(R.id.bnv_main);
        bNav.getMenu().getItem(0).setChecked(true);
        bNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.item_timetable:
                        item.setChecked(true);
                        if (timetableFragment == null) {
                            timetableFragment = new TimetableFragment();
                        }
                        fragmentTransaction.replace(R.id.fl_main_content, timetableFragment);
                        break;
                    case R.id.item_search:
                        item.setChecked(true);
                        if (searchFragment == null) {
                            searchFragment = new SearchFragment();
                        }
                        fragmentTransaction.replace(R.id.fl_main_content, searchFragment);
                        break;
                    case R.id.item_me:
                        item.setChecked(true);
                        if (meFragment == null) {
                            meFragment = new MeFragment();
                        }
                        fragmentTransaction.replace(R.id.fl_main_content, meFragment);
                        break;
                }
                fragmentTransaction.commit();
                return false;
            }
        });
    }

}