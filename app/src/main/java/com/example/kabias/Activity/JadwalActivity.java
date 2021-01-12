package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.kabias.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class JadwalActivity extends AppCompatActivity {

    private LinearLayout linearSMOKT, linearSMOSLO, linearSLOKT, linearKTSMO, linearSLOSMO, linearKTSLO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        Window window = JadwalActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(JadwalActivity.this,R.color.colorPrimaryDark));


        initView();

        bottomNav();

    }



    private void initView() {

        linearSMOKT = findViewById(R.id.linearSMOKT);
        linearSMOSLO = findViewById(R.id.linearSMOSLO);
        linearSLOKT = findViewById(R.id.linearSLOKT);
        linearKTSMO = findViewById(R.id.linearKTSMO);
        linearSLOSMO = findViewById(R.id.linearSLOSMO);
        linearKTSLO = findViewById(R.id.linearKTSLO);

        categorySMOKT();
        categorySMOSLO();
        categorySLOKT();
        categoryKTSMO();
        categorySLOSMO();
        categoryKTSLO();

    }

    private void categorySMOKT() {

        linearSMOKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
                intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "SMOKT");
                intent.putExtra("title", "Bandara Adi Soemarmo - Klaten");
                startActivity(intent);
            }
        });
    }

    private void categorySMOSLO() {

        linearSMOSLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
                intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "SMOSLO");
                intent.putExtra("title", "Bandara Adi Soemarmo - Solo Balapan");
                startActivity(intent);
            }
        });

    }

    private void categorySLOKT() {

        linearSLOKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
                intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "SLOKT");
                intent.putExtra("title", "Solo Balapan - Klaten");
                startActivity(intent);
            }
        });

    }

    private void categoryKTSMO() {
        linearKTSMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
        intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "KTSMO");
        intent.putExtra("title", "Klaten - Bandara Adi Soemarmo");
        startActivity(intent);
            }
        });
    }

    private void categoryKTSLO() {
        linearKTSLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
                intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "KTSLO");
                intent.putExtra("title", "Klaten - Solo Balapan");
                startActivity(intent);
            }
        });
    }

    private void categorySLOSMO() {
        linearSLOSMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JadwalActivity.this, ListJadwalActivity.class);
                intent.putExtra(ListJadwalActivity.KEY_RUTE_CATEGORY, "SLOSMO");
                intent.putExtra("title", "Solo Balapan - Bandara Adi Soemarmo");
                startActivity(intent);
            }
        });
    }

    private void bottomNav() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.jadwal);
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
                        return true;
//
                    case R.id.peta:
                        startActivity(new Intent(getApplicationContext(), PetaRuteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
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