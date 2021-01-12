package com.example.kabias.Activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kabias.Adapter.DetailJadwalAdapter;
import com.example.kabias.Adapter.JadwalAdapter;
import com.example.kabias.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class DetailJadwalActivity extends AppCompatActivity implements DetailJadwalAdapter.OnProductSelectedListener {

    public static final String KEY_ID_CATEGORY = "key_jadwal_category";
    private static final String TAG = "ListJadwalActivity";

    private FirebaseFirestore mFirestore;
    private Query mQueryBerangkat, mQueryDatang;
    private DetailJadwalAdapter mAdapterBerangkat;
    private DetailJadwalAdapter mAdapterDatang;

    private LinearLayout linearBerangkat, linearDatang;
    private RecyclerView rvListBerangkat, rvListDatang;
    private TextView tvTitle;
    private ImageView ic_back;

    private String documentId, title;

    private boolean emptyBerangkat;
    private boolean emptyDatang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        Window window = DetailJadwalActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(DetailJadwalActivity.this,R.color.colorPrimaryDark));

        documentId = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_ID_CATEGORY);
        Log.e(TAG, "onCreate: kategori" + documentId);

        initFirestore();
        initView();
        initRv();
    }

    private void initView() {

        linearBerangkat = findViewById(R.id.linearBerangkat);
        linearDatang = findViewById(R.id.linearDatang);
        rvListBerangkat = findViewById(R.id.rvListBerangkat);
        rvListDatang = findViewById(R.id.rvListDatang);
        tvTitle = findViewById(R.id.tvTitle);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        tvTitle.setText(title);
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();
        mQueryBerangkat = mFirestore.collection("Jadwal").document(documentId).collection("ListJadwal").whereEqualTo("kategori", "Keberangkatan");
        mQueryDatang = mFirestore.collection("Jadwal").document(documentId).collection("ListJadwal").whereEqualTo("kategori", "Kedatangan");

    }

    private void initRv() {

        if (mQueryBerangkat == null && mQueryDatang == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }
        else {
            Log.w(TAG, "semua");
            rvBerangkat();
            rvDatang();
        }

    }


    private void rvBerangkat() {

        mAdapterBerangkat = new DetailJadwalAdapter(mQueryBerangkat, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    emptyBerangkat = true;
                    Log.e(String.valueOf(getItemCount()), "item");
                } else {

                    emptyBerangkat = false;
                    rvListBerangkat.setVisibility(View.VISIBLE);
                    linearBerangkat.setVisibility(View.VISIBLE);

                    Log.w(TAG, "Show Produk");
                }
                emptyJadwal();

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        rvListBerangkat.setLayoutManager(new LinearLayoutManager(DetailJadwalActivity.this));
        rvListBerangkat.setAdapter(mAdapterBerangkat);
    }


    private void rvDatang() {

        mAdapterDatang = new DetailJadwalAdapter(mQueryDatang, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {

                    emptyDatang = true;

                    Log.e(String.valueOf(getItemCount()), "item");
                } else {

                    emptyDatang = false;
                    rvListDatang.setVisibility(View.VISIBLE);
                    linearDatang.setVisibility(View.VISIBLE);

                    Log.w(TAG, "Show Produk");
                }
                emptyJadwal();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        rvListDatang.setLayoutManager(new LinearLayoutManager(DetailJadwalActivity.this));
        rvListDatang.setAdapter(mAdapterDatang);
    }


    private void emptyJadwal() {

        if (emptyBerangkat && emptyDatang) {

            linearBerangkat.setVisibility(View.GONE);
            linearDatang.setVisibility(View.GONE);

        } else if (emptyBerangkat) {

            rvListBerangkat.setVisibility(View.GONE);
            linearBerangkat.setVisibility(View.GONE);

        } else if (emptyDatang) {

            rvListDatang.setVisibility(View.GONE);
            linearDatang.setVisibility(View.GONE);
        }


    }



    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (mAdapterBerangkat != null && mAdapterDatang !=null) {
            mAdapterBerangkat.startListening();
            mAdapterDatang.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterBerangkat != null && mAdapterDatang !=null) {
            mAdapterBerangkat.startListening();
            mAdapterDatang.startListening();
        }
    }

    @Override
    public void onProductSelected(DocumentSnapshot listMitraModel) {

    }
}