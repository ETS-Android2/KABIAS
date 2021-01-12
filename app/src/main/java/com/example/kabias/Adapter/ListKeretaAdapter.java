package com.example.kabias.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kabias.Model.ListKeretaModel;
import com.example.kabias.Model.ListStasiunModel;
import com.example.kabias.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class ListKeretaAdapter extends FirestoreAdapter<ListKeretaAdapter.ViewHolder> {

    private final OnProductSelectedListener mListener;

    public ListKeretaAdapter(Query query, ListKeretaAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_rv_list_kereta, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot listMitraModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAwal;
        private final TextView tvTujuan;
        private final TextView tvKereta;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAwal = itemView.findViewById(R.id.tvAwal);
            tvTujuan = itemView.findViewById(R.id.tvTujuan);
            tvKereta = itemView.findViewById(R.id.tvKereta);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            Log.e("id", String.valueOf(snapshot));
            ListKeretaModel listKeretaModel = snapshot.toObject(ListKeretaModel.class);

            tvAwal.setText(listKeretaModel.getAwal());
            tvTujuan.setText(listKeretaModel.getTujuan());
            tvKereta.setText(listKeretaModel.getKereta());

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

