package com.example.kabias.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kabias.Activity.DetailStasiunActivity;
import com.example.kabias.Adapter.ListStasiunAdapter;
import com.example.kabias.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class InformasiStasiunFragment extends Fragment implements ListStasiunAdapter.OnProductSelectedListener {

    private static final String TAG = "StasiunFragment";

    private RecyclerView rvListStasiun;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ListStasiunAdapter mAdapter;
    private String stasiunId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Stasiun");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_informasi_stasiun, container, false);

        rvListStasiun = v.findViewById(R.id.rvListStasiun);


        initRv();
        return v;
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
                Log.w(TAG, "Error" + e);
            }
        };

        rvListStasiun.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onProductSelected(DocumentSnapshot listStasiunModel) {
        Intent intent = new Intent(getActivity(), DetailStasiunActivity.class);
        intent.putExtra(DetailStasiunActivity.KEY_STASIUN_ID, listStasiunModel.getId());

        startActivity(intent);

    }

}

