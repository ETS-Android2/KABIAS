package com.example.kabias.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kabias.Fragment.InformasiKeretaFragment;
import com.example.kabias.Fragment.InformasiStasiunFragment;

public class InformasiAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;

    public InformasiAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new InformasiKeretaFragment();
                return fragment;

            case 1:
                fragment = new InformasiStasiunFragment();
                return fragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

