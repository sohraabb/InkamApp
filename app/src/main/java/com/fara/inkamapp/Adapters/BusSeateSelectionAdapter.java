package com.fara.inkamapp.Adapters;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fara.inkamapp.Fragments.ChooseSeat;

import java.util.List;

public class BusSeateSelectionAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public BusSeateSelectionAdapter(@NonNull FragmentManager fm,List<Fragment> fragments) {

        super(fm);
       this.fragments=fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return  this.fragments.size();
    }

}
