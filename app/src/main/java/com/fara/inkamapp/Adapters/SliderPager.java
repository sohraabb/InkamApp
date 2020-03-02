package com.fara.inkamapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fara.inkamapp.Fragments.SliderItem;

public class SliderPager extends FragmentPagerAdapter {
    public SliderPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull @Override public Fragment getItem(int position) {
        return SliderItem.newInstance(position);
    }
    // size is hardcoded
    @Override public int getCount() {
        return 3;
    }
}