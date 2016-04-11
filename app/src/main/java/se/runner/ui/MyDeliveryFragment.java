package se.runner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.widget.TaskListFragment;

public class MyDeliveryFragment extends Fragment {

    private MaterialTabs mydelivery_tabs;
    private ViewPager mydelivery_view_pager;

    private RunnerPagerAdapter runnerPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydelivery, container, false);
        mydelivery_tabs = (MaterialTabs) view.findViewById(R.id.mydelivery_tabs);
        mydelivery_view_pager = (ViewPager) view.findViewById(R.id.mydelivery_view_pager);

        runnerPagerAdapter = new RunnerPagerAdapter(getContext(), getChildFragmentManager());
        runnerPagerAdapter.setItem(getContext().getString(R.string.all_delivey), new MyDeliveryListFragment(), null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.deliverying), new MyDeliveryListFragment(), null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.deliveryed), new MyDeliveryListFragment(), null);
        mydelivery_view_pager.setAdapter(runnerPagerAdapter);
        mydelivery_tabs.setViewPager(mydelivery_view_pager);
        return view;
    }
}