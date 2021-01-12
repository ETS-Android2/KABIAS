package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kabias.Adapter.JadwalAdapter;
import com.example.kabias.Adapter.ListKeretaAdapter;
import com.example.kabias.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ListJadwalActivity extends AppCompatActivity implements JadwalAdapter.OnProductSelectedListener {

    public static final String KEY_RUTE_CATEGORY = "key_jadwal_category";

    private static final String TAG = "ListJadwalActivity";

    private ImageView ic_back;
    private TextView tvTitle, tvCodeKereta;
    private RecyclerView rvListJadwal;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private JadwalAdapter mAdapter;
    private String jadwalCategory, keretaCategory, jadwalId, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jadwal);

        Window window = ListJadwalActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ListJadwalActivity.this,R.color.colorPrimaryDark));


        jadwalCategory = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_RUTE_CATEGORY);

        Log.e(TAG, "onCreate: kategori" + jadwalCategory);

        initFirestore();
        initView();
        initRv();
    }



    private void initView() {
        //back
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = findViewById(R.id.tvTitle);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        tvTitle.setText(title);
        rvListJadwal = findViewById(R.id.rvListJadwal);

    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Jadwal").whereEqualTo("rute", jadwalCategory);

    }


    private void initRv() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new JadwalAdapter(mQuery, this) {

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

        rvListJadwal.setLayoutManager(new LinearLayoutManager(ListJadwalActivity.this));
        rvListJadwal.setAdapter(mAdapter);
    }

    @Override
    public void onProductSelected(DocumentSnapshot listMitraModel) {
        Intent intent = new Intent(ListJadwalActivity.this, DetailJadwalActivity.class);
        intent.putExtra(DetailJadwalActivity.KEY_ID_CATEGORY, listMitraModel.getId());
        intent.putExtra("title", listMitraModel.getString("stasiun"));
        startActivity(intent);
        Log.e(listMitraModel.getId(), "ididid");
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
}