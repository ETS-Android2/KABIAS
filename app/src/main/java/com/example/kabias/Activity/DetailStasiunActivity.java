package com.example.kabias.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kabias.Adapter.ViewPagerAdapter;
import com.example.kabias.Model.ListStasiunModel;
import com.example.kabias.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DetailStasiunActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    public static final String KEY_STASIUN_ID = "key_stasiun_id";
    private static final String TAG = "Detail Activity";


    private LinearLayout linearHotel, linearAtm, linearRestoran, linearMall, linearMarket, linearTerminal;
    private ImageView ic_back;
    private TextView tvStasiunName, tvAddress;


    private FirebaseFirestore mFirestore;
    private DocumentReference mStasiunRef;
    private ListenerRegistration mProductRegistration;

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private String[] img;
    private String image1, image2, image3, stasiunId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stasiun);

        Window window = DetailStasiunActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(DetailStasiunActivity.this,R.color.colorPrimaryDark));

        initFirestore();
        initView();
    }

    private void initFirestore() {

        stasiunId = getIntent().getExtras().getString(KEY_STASIUN_ID);
        if (stasiunId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_STASIUN_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mStasiunRef = mFirestore.collection("Stasiun").document(stasiunId);


    }

    private void initView() {

        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvStasiunName = findViewById(R.id.tvStasiunName);
        tvAddress = findViewById(R.id.tvAddress);
        linearHotel = findViewById(R.id.linearHotel);
        linearAtm = findViewById(R.id.linearAtm);
        linearRestoran = findViewById(R.id.linearRestoran);
        linearMall = findViewById(R.id.linearMall);
        linearMarket = findViewById(R.id.linearMarket);
        linearTerminal = findViewById(R.id.linearTerminal);

    }

    @Override
    public void onStart() {
        super.onStart();

        mProductRegistration = mStasiunRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mProductRegistration != null) {
            mProductRegistration.remove();
            mProductRegistration = null;
        }
    }


    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            Log.w(TAG, "product:onEvent", error);
            return;
        }

        onProductLoaded(Objects.requireNonNull(Objects.requireNonNull(snapshot).toObject(ListStasiunModel.class)));

    }


    private void onProductLoaded(final ListStasiunModel listStasiunModel) {

        tvStasiunName.setText("Stasiun " + listStasiunModel.getName());
        tvAddress.setText(listStasiunModel.getAddress());
        if (listStasiunModel.isAtm()) {
        linearAtm.setVisibility(View.VISIBLE);
        }
        if (listStasiunModel.isHotel()) {
            linearHotel.setVisibility(View.VISIBLE);
        }
        if (listStasiunModel.isMall()) {
            linearMall.setVisibility(View.VISIBLE);
        }
        if (listStasiunModel.isMarket()) {
            linearMarket.setVisibility(View.VISIBLE);
        }
        if (listStasiunModel.isRestoran()) {
            linearRestoran.setVisibility(View.VISIBLE);
        }
        if (listStasiunModel.isTerminal()) {
            linearTerminal.setVisibility(View.VISIBLE);
        }

        Log.e(String.valueOf(listStasiunModel.isAtm()), "atm");
        Log.e(String.valueOf(listStasiunModel.isHotel()), "hotel");
        Log.e(String.valueOf(listStasiunModel.isMall()), "mall");
        Log.e(String.valueOf(listStasiunModel.isMarket()), "market");

        image1 = listStasiunModel.getImage1();
        image2 = listStasiunModel.getImage2();
        image3 = listStasiunModel.getImage3();

        img = new String[]{image1, image2, image3};
        //view pager code
        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(DetailStasiunActivity.this, img);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);


    }

}