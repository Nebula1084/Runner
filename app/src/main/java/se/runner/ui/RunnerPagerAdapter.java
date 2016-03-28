package se.runner.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class RunnerPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;

    public RunnerPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    public void setItem(String title, Fragment fragment) {
        titles.add(title);
        fragments.add(fragment);
    }

    public void removeItem(int position) {
        titles.remove(position);
        fragments.remove(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }
}
