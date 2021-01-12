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
import android.widget.TextView;

import com.example.kabias.Adapter.ListStasiunAdapter;
import com.example.kabias.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ListStasiunActivity extends AppCompatActivity implements ListStasiunAdapter.OnProductSelectedListener {


    private static final String TAG = "ListStasiunActivity";

    private RecyclerView rvListStasiun;
    private TextView tvTitle;
    private ImageView ic_back;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ListStasiunAdapter mAdapter;
    private String stasiunId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stasiun);

        Window window = ListStasiunActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ListStasiunActivity.this,R.color.colorPrimaryDark));

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Stasiun");

        initView();
        initRv();
    }

    private void initView() {

        rvListStasiun = findViewById(R.id.rvListStasiun);

        //title
        tvTitle = findViewById(R.id.tvTitle);

        //back
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initRv() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ListStasiunAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rvListStasiun.setVisibility(View.GONE);
                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvListStasiun.setVisibility(View.VISIBLE);
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

        rvListStasiun.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvListStasiun.setAdapter(mAdapter);
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

        String name = listMitraModel.getString("name");
        String code = listMitraModel.getString("code");

        if (getIntent().hasExtra("keyActivity")) {
            String keyActivity = getIntent().getStringExtra("keyActivity");
            if (keyActivity.equals("awal")) {
                Intent mIntent = new Intent();
                mIntent.putExtra("stasiunAwal", name);
                mIntent.putExtra("codeStasiunAwal", code);
                mIntent.putExtra("keyActivity", "awal");
                setResult(RESULT_OK, mIntent);
                finish();
            } else  {
                Intent mIntent = new Intent();
                mIntent.putExtra("stasiunTujuan", name);
                mIntent.putExtra("codeStasiunTujuan", code);
                mIntent.putExtra("keyActivity", "tujuan");
                setResult(RESULT_OK, mIntent);
                finish();
            }
        }



    }
}