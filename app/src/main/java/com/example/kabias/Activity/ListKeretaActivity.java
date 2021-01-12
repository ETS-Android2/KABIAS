package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kabias.Adapter.ListKeretaAdapter;
import com.example.kabias.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ListKeretaActivity extends AppCompatActivity implements ListKeretaAdapter.OnProductSelectedListener {

    public static final String KEY_CATEGORY = "key_category";
    private static final String TAG = "ListKeretaActivity";

    private ImageView ic_back, icHelp;
    private TextView tvStasiunAwal, tvStasiunTujuan, tvDate, tvPenumpang, tvTarif, tvEstimasi;
    private RecyclerView rvListJadwal;
    private Button btnMaps;

    private String codeStasiunAwal, codeStasiunTujuan, date, penumpang;
    private String estimasi, maps, tarif;

    private FirebaseFirestore mFirestore;
    private DocumentReference mDataRef;
    private Query mQuery;
    private ListKeretaAdapter mAdapter;

    private String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kereta);

        Window window = ListKeretaActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ListKeretaActivity.this,R.color.colorPrimaryDark));

        mCategory = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_CATEGORY);
        Log.e(TAG, "onCreate: kategori" + mCategory);

        initFirestore();
        initView();
        initData();
        getData();
        initRv();
    }

    private void initData() {

        mDataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "Document exists!");
                        estimasi = document.getString("estimasi");
                        maps = document.getString("maps");
                        Locale localeID = new Locale("in", "ID");
                        tarif = NumberFormat.getCurrencyInstance(localeID).format(document.getLong("tarif").intValue());

                        tvTarif.setText(tarif + " /org");
                        tvEstimasi.setText(estimasi);

                        Log.d(TAG, "Document users Exists!");

                    } else {

                        Log.d(TAG, "Document does not exist!");

                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void initRv() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ListKeretaAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rvListJadwal.setVisibility(View.GONE);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvListJadwal.setVisibility(View.VISIBLE);

                    Log.w(TAG, "Show Shops");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        rvListJadwal.setLayoutManager(new LinearLayoutManager(ListKeretaActivity.this));
        rvListJadwal.setAdapter(mAdapter);
    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Cari").document(mCategory).collection("ListJadwal").whereEqualTo("rute",mCategory);

        mDataRef = mFirestore.collection("Cari").document(mCategory);

    }

    private void getData() {
        Intent intent = getIntent();
        codeStasiunAwal = intent.getStringExtra("codeStasiunAwal");
        codeStasiunTujuan = intent.getStringExtra("codeStasiunTujuan");
        date = intent.getStringExtra("date");
        penumpang = intent.getStringExtra("penumpang");

        tvStasiunAwal.setText(codeStasiunAwal);
        tvStasiunTujuan.setText(codeStasiunTujuan);
        tvDate.setText(date);
        tvPenumpang.setText(penumpang);
    }

    private void initView() {

        ic_back = findViewById(R.id.ic_back);
        tvStasiunAwal = findViewById(R.id.tvStasiunAwal);
        tvStasiunTujuan = findViewById(R.id.tvStasiunTujuan);
        tvDate = findViewById(R.id.tvDate);
        tvPenumpang = findViewById(R.id.tvPenumpang);
        rvListJadwal = findViewById(R.id.rvListJadwal);
        tvTarif = findViewById(R.id.tvTarif);
        tvEstimasi = findViewById(R.id.tvEstimasi);
        btnMaps = findViewById(R.id.btnMaps);
        icHelp = findViewById(R.id.icHelp);

        icHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tarif Harga dapat berubah sewaktu-waktu.", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps();
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void openMaps() {
        String url = maps;
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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
    public void onProductSelected(DocumentSnapshot listMitraModel) {

    }
}