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

public class MyTaskFragment extends Fragment {

    private MaterialTabs mytask_tabs;
    private ViewPager mytask_view_pager;

    private RunnerPagerAdapter runnerPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytask, container, false);
        mytask_tabs = (MaterialTabs) view.findViewById(R.id.mytask_tabs);
        mytask_view_pager = (ViewPager) view.findViewById(R.id.mytask_view_pager);

        runnerPagerAdapter = new RunnerPagerAdapter(getContext(), getChildFragmentManager());
        runnerPagerAdapter.setItem(getContext().getString(R.string.all_task), TaskListFragment.newInstance(R.layout.item_task_small), null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.not_delivered), TaskListFragment.newInstance(R.layout.item_task_small), null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.task_finished), TaskListFragment.newInstance(R.layout.item_task_small), null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.task_unavailable), TaskListFragment.newInstance(R.layout.item_task_small), null);

        mytask_view_pager.setAdapter(runnerPagerAdapter);
        mytask_tabs.setViewPager(mytask_view_pager);
        return view;
    }
}