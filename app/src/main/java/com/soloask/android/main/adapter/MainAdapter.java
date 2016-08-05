package com.soloask.android.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soloask.android.main.view.impl.HotFragment;
import com.soloask.android.main.view.impl.PersonFragment;

/**
 * Created by LeBron on 2016/5/18.
 */
public class MainAdapter extends FragmentPagerAdapter {
    private String[] tabs;

    public MainAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HotFragment();
                break;
            case 1:
                fragment = new PersonFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        if (tabs != null) {
            return tabs.length;
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
