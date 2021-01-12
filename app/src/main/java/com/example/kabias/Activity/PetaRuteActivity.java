package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.kabias.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PetaRuteActivity extends AppCompatActivity {

    private Button btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_rute);

        Window window = PetaRuteActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(PetaRuteActivity.this,R.color.colorPrimaryDark));


        initView();
        bottomNav();

    }

    private void initView() {

        btnMaps = findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com/maps/@-7.6111821,110.5102224,10z/data=!3m1!4b1!4m2!6m1!1s1Tih0I5O6kFW9nSb7ctJXN8Zs9OXF3IA6";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

    }

    private void bottomNav() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.peta);
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
                        return true;
//
                    case R.id.informasi:
                        startActivity(new Intent(getApplicationContext(), InformasiActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}