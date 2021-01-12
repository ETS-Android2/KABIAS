package com.example.kabias.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.kabias.Adapter.AdsHomeAdapter;
import com.example.kabias.CirclePagerIndicatorDecoration;
import com.example.kabias.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static android.content.Context.MODE_PRIVATE;

public class InformasiKeretaFragment extends Fragment implements AdsHomeAdapter.OnProductSelectedListener {


    private static final String TAG = "KeretaFragment";

    //Image
    private RecyclerView rvKereta;
    private Query mQuery;
    private AdsHomeAdapter mAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Beranda");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_informasi_kereta, container, false);


        rvKereta = v.findViewById(R.id.rvKereta);

        initRv();
        return v;
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
                    rvKereta.setVisibility(View.GONE);
                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvKereta.setVisibility(View.VISIBLE);
                    Log.w(TAG, "Show Produk");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Log.w(TAG, "Error" + e);
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvKereta.setLayoutManager(layoutManager);
        rvKereta.setHasFixedSize(true);
        rvKereta.setAdapter(mAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvKereta);
        rvKereta.addItemDecoration(new CirclePagerIndicatorDecoration());
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
