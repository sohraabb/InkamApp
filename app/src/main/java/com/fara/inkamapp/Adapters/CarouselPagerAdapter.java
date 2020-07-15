package com.fara.inkamapp.Adapters;


import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fara.inkamapp.Activities.BusServices;

import com.fara.inkamapp.Fragments.ItemFragment;
import com.fara.inkamapp.Helpers.CarouselLinearLayout;
import com.fara.inkamapp.R;

import java.text.ParseException;
import java.util.List;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener  {

    public final static float BIG_SCALE = 0.5f;
    public final static float SMALL_SCALE = 0.5f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private BusServices context;
    private FragmentManager fragmentManager;
    private float scale;
    private String currentDate;
    private TextView selectedDay, selectedDate;
    public CarouselPagerAdapter(BusServices context, FragmentManager fm,String currentDate) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.currentDate=currentDate;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == BusServices.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % BusServices.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Fragment frg= ItemFragment.newInstance(context, position, scale,currentDate);

        return  frg;
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = BusServices.count * BusServices.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }

}