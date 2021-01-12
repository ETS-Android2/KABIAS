package com.example.kabias.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kabias.Model.JadwalModel;
import com.example.kabias.Model.ListKeretaModel;
import com.example.kabias.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class JadwalAdapter extends FirestoreAdapter<JadwalAdapter.ViewHolder> {

    private final OnProductSelectedListener mListener;

    public JadwalAdapter(Query query, JadwalAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_rv_jadwal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot listMitraModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRuteNameStasiun;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRuteNameStasiun = itemView.findViewById(R.id.tvRuteNameStasiun);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            Log.e("id", String.valueOf(snapshot));
            JadwalModel jadwalModel = snapshot.toObject(JadwalModel.class);

            tvRuteNameStasiun.setText(jadwalModel.getStasiun());

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
