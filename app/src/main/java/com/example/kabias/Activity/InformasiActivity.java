package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.kabias.Adapter.InformasiAdapter;
import com.example.kabias.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class InformasiActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InformasiAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        Window window = InformasiActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(InformasiActivity.this,R.color.colorPrimaryDark));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new InformasiAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout();
        bottomNav();

    }

    private void tabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    private void bottomNav() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.informasi);
        // item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.antar:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.jadwal:
                        startActivity(new Intent(getApplicationContext(), JadwalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
//
                    case R.id.peta:
                        startActivity(new Intent(getApplicationContext(), PetaRuteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
//
                    case R.id.informasi:
                        return true;
                }
                return false;
            }
        });
    }
}