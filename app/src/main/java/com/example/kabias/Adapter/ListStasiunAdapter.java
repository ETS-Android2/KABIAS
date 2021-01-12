package com.example.kabias.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kabias.Model.ListStasiunModel;
import com.example.kabias.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class ListStasiunAdapter extends FirestoreAdapter<ListStasiunAdapter.ViewHolder> {

    private final OnProductSelectedListener mListener;

    public ListStasiunAdapter(Query query, ListStasiunAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_rv_list_stasiun, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot listMitraModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNameStasiun;
        private final TextView tvCodeStasiun;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameStasiun = itemView.findViewById(R.id.tvNameStasiun);
            tvCodeStasiun = itemView.findViewById(R.id.tvCodeStasiun);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            Log.e("id", String.valueOf(snapshot));
            ListStasiunModel listStasiunModel = snapshot.toObject(ListStasiunModel.class);




            tvNameStasiun.setText(listStasiunModel.getName());
            tvCodeStasiun.setText(listStasiunModel.getCode());



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
