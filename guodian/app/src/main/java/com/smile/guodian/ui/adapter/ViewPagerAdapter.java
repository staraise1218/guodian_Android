package com.smile.guodian.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smile.guodian.ui.fragment.NavCartFragment;
import com.smile.guodian.ui.fragment.NavHelpFragment;
import com.smile.guodian.ui.fragment.NavHomeFragment;
import com.smile.guodian.ui.fragment.NavMyFragment;
import com.smile.guodian.ui.fragment.NavWeFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(NavHomeFragment.newInstance());
        fragments.add(NavWeFragment.newInstance());
        fragments.add(NavHelpFragment.newInstance());
        fragments.add(NavCartFragment.newInstance());
        fragments.add(NavMyFragment.newInstance());

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}