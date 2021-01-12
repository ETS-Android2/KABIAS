package com.example.kabias.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kabias.Model.AdsHomeModel;
import com.example.kabias.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class AdsHomeAdapter extends FirestoreAdapter<AdsHomeAdapter.ViewHolder> {

    private OnProductSelectedListener mListener;

    public AdsHomeAdapter(Query query, OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_rv_ads_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot productModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_home;

        public ViewHolder(View itemView) {
            super(itemView);
            img_home = itemView.findViewById(R.id.img_home);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            AdsHomeModel adsHomeModel = snapshot.toObject(AdsHomeModel.class);

            // Load image
            Glide.with(img_home.getContext())
                    .load(adsHomeModel.getImage())
                    .into(img_home);

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProductSelected(snapshot);
                    }
                }
            });
        }

    }
}
