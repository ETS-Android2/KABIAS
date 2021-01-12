package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kabias.Adapter.AdsHomeAdapter;
import com.example.kabias.CirclePagerIndicatorDecoration;
import com.example.kabias.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdsHomeAdapter.OnProductSelectedListener {

    private static final String TAG = "MainActivity";

    private EditText etStasiunAwal, etStasiunTujuan, etDate;
    private TextView tvPenumpang;
    private Button btnCari;
    private LinearLayout btnSwitch;

    private RadioGroup mKategori;
    private RadioButton mKategoriOption;
    private String kategori, stasiunAwal, codeStasiunAwal, codeStasiunTujuan, stasiunTujuan, date, penumpang;

    //Image
    private RecyclerView rvHome;
    private Query mQuery;
    private AdsHomeAdapter mAdapter;
    private FirebaseFirestore mFirestore;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = MainActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark));
//Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Beranda");

        initView();
        initRv();
        initOnClick();
        bottomNav();
    }

    private void initRv() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new AdsHomeAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rvHome.setVisibility(View.GONE);
                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvHome.setVisibility(View.VISIBLE);
                    Log.w(TAG, "Show Produk");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHome.setLayoutManager(layoutManager);
        rvHome.setHasFixedSize(true);
        rvHome.setAdapter(mAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvHome);
        rvHome.addItemDecoration(new CirclePagerIndicatorDecoration());
    }

    private void initOnClick() {

        //stasiunAwal
        etStasiunAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, ListStasiunActivity.class);
                mIntent.putExtra("keyActivity", "awal");
                startActivityForResult(mIntent, 1);
            }
        });
        //stasiunTujuan
        etStasiunTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, ListStasiunActivity.class);
                mIntent.putExtra("keyActivity", "tujuan");
                startActivityForResult(mIntent, 2);
            }
        });
        //Date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE" + ", " + "dd MMMM yyyy", Locale.getDefault());
                        calendar.set(year, month, day);
                        String dateString = sdf.format(calendar.getTime());
                        etDate.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //Penumpang
        tvPenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_penumpang, null);
                Button pilih = (Button) view.findViewById(R.id.btn_pilih_kategori);
                mKategori = (RadioGroup) view.findViewById(R.id.rg_kategori);
                mKategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        mKategoriOption = mKategori.findViewById(checkedId);
                        switch (checkedId) {
                            case R.id.p1:
                            case R.id.p2:
                            case R.id.p3:
                            case R.id.p4:
                            case R.id.p5:
                                kategori = mKategoriOption.getText().toString();
                                break;
                            default:
                        }
                    }
                });
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                pilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String value_kategori = mKategoriOption.getText().toString();
                        tvPenumpang.setText(value_kategori);
                        dialog.dismiss();
                    }
                });
            }
        });
        //Cari
        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cariKereta();
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String awal = etStasiunAwal.getText().toString();
                String tujuan = etStasiunTujuan.getText().toString();
                if (awal.isEmpty() && tujuan.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Stasiun Awal dan Tujuan tidak boleh kosong.",
                            Toast.LENGTH_LONG).show();
                } else {
                    etStasiunAwal.setText(tujuan);
                    etStasiunTujuan.setText(awal);
                }
            }
        });

    }

    private void cariKereta() {

        String dari = etStasiunAwal.getText().toString();
        String ke = etStasiunTujuan.getText().toString();
        date = etDate.getText().toString();
        penumpang = tvPenumpang.getText().toString();

        if (TextUtils.isEmpty(dari)) {
            Toast.makeText(getApplicationContext(), "Stasiun Awal tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ke)) {
            Toast.makeText(getApplicationContext(), "Stasiun Tujuan tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(date)) {
            Toast.makeText(getApplicationContext(), "Tanggal Pergi tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (codeStasiunAwal.equals(codeStasiunTujuan) && !TextUtils.isEmpty(date)) {
            Toast.makeText(getApplicationContext(), "Stasiun Awal/Tujuan tidak boleh sama.", Toast.LENGTH_SHORT).show();
            return;
        }
            //SMOKDO
        if (codeStasiunAwal.equals("SMO") && codeStasiunTujuan.equals("KDO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SMOKDO");
            startActivity(intent);
            //SMOSLO
        } else if (codeStasiunAwal.equals("SMO") && codeStasiunTujuan.equals("SLO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SMOSLO");
            startActivity(intent);
            //SMOPWS
        } else if (codeStasiunAwal.equals("SMO") && codeStasiunTujuan.equals("PWS")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SMOPWS");
            startActivity(intent);
            //SMOKT
        } else if (codeStasiunAwal.equals("SMO") && codeStasiunTujuan.equals("KT")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SMOKT");
            startActivity(intent);
            //KDOSLO
        } else if (codeStasiunAwal.equals("KDO") && codeStasiunTujuan.equals("SLO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KDOSLO");
            startActivity(intent);
            //KDOPWS
        } else if (codeStasiunAwal.equals("KDO") && codeStasiunTujuan.equals("PWS")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KDOPWS");
            startActivity(intent);
            //KDOKT
        } else if (codeStasiunAwal.equals("KDO") && codeStasiunTujuan.equals("KT")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KDOKT");
            startActivity(intent);
            //SLOPWS
        } else if (codeStasiunAwal.equals("SLO") && codeStasiunTujuan.equals("PWS")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SLOPWS");
            startActivity(intent);
            //SLOKT
        } else if (codeStasiunAwal.equals("SLO") && codeStasiunTujuan.equals("KT")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SLOKT");
            startActivity(intent);
            //PWSKT
        } else if (codeStasiunAwal.equals("PWS") && codeStasiunTujuan.equals("KT")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "PWSKT");
            startActivity(intent);
            //KTPWS
        } else if (codeStasiunAwal.equals("KT") && codeStasiunTujuan.equals("PWS")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KTPWS");
            startActivity(intent);
            //KTSLO
        } else if (codeStasiunAwal.equals("KT") && codeStasiunTujuan.equals("SLO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KTSLO");
            startActivity(intent);
            //KTKDO
        } else if (codeStasiunAwal.equals("KT") && codeStasiunTujuan.equals("KDO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KTKDO");
            startActivity(intent);
            //KTSMO
        } else if (codeStasiunAwal.equals("KT") && codeStasiunTujuan.equals("SMO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KTSMO");
            startActivity(intent);
            //PWSSLO
        } else if (codeStasiunAwal.equals("PWS") && codeStasiunTujuan.equals("SLO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "PWSSLO");
            startActivity(intent);
            //PWSKDO
        } else if (codeStasiunAwal.equals("PWS") && codeStasiunTujuan.equals("KDO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SMOKT");
            startActivity(intent);
            //PWSSMO
        } else if (codeStasiunAwal.equals("PWS") && codeStasiunTujuan.equals("SMO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "PWSSMO");
            startActivity(intent);
            //SLOKDO
        } else if (codeStasiunAwal.equals("SLO") && codeStasiunTujuan.equals("KDO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SLOKDO");
            startActivity(intent);
            //SLOSMO
        } else if (codeStasiunAwal.equals("SLO") && codeStasiunTujuan.equals("SMO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "SLOSMO");
            startActivity(intent);
            //KDOSMO
        } else if (codeStasiunAwal.equals("KDO") && codeStasiunTujuan.equals("SMO")) {
            Intent intent = new Intent(MainActivity.this, ListKeretaActivity.class);
            intent.putExtra("codeStasiunAwal", codeStasiunAwal);
            intent.putExtra("codeStasiunTujuan", codeStasiunTujuan);
            intent.putExtra("date", date);
            intent.putExtra("penumpang", penumpang);
            intent.putExtra(ListKeretaActivity.KEY_CATEGORY, "KDOSMO");
            startActivity(intent);
        }


    }

    private void initView() {

        etStasiunAwal = findViewById(R.id.etStasiunAwal);
        etStasiunTujuan = findViewById(R.id.etStasiunTujuan);
        tvPenumpang = findViewById(R.id.tvPenumpang);
        etDate = findViewById(R.id.etDate);
        btnCari = findViewById(R.id.btnCari);
        btnSwitch = findViewById(R.id.linearLayout3);
        rvHome = findViewById(R.id.rvHome);

    }

    private void bottomNav() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.antar);
        // item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.antar:
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
                        startActivity(new Intent(getApplicationContext(), InformasiActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                stasiunAwal = data.getStringExtra("stasiunAwal");
                codeStasiunAwal = data.getStringExtra("codeStasiunAwal");
                etStasiunAwal.setText(stasiunAwal + " (" + codeStasiunAwal + ")");
                break;

            case 2:
                stasiunTujuan = data.getStringExtra("stasiunTujuan");
                codeStasiunTujuan = data.getStringExtra("codeStasiunTujuan");
                etStasiunTujuan.setText(stasiunTujuan + " (" + codeStasiunTujuan + ")");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi, untuk keluar!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onProductSelected(DocumentSnapshot productModel) {

    }
}