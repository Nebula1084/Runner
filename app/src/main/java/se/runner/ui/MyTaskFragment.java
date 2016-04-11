package se.runner.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.user.User;
import se.runner.widget.TaskListFragment;

public class MyTaskFragment extends Fragment
{
    private final static String TAG="MyTaskFragment";
    private MaterialTabs mytask_tabs;
    private ViewPager mytask_view_pager;

    private RunnerPagerAdapter runnerPagerAdapter;

    private User user;

    private MyTaskListFragment task_list_frg_all;
    private MyTaskListFragment task_list_frg_wait_Acpt;
    private MyTaskListFragment task_list_frg_completed;
    private MyTaskListFragment task_list_frg_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytask, container, false);
        mytask_tabs = (MaterialTabs) view.findViewById(R.id.mytask_tabs);
        mytask_view_pager = (ViewPager) view.findViewById(R.id.mytask_view_pager);

        runnerPagerAdapter = new RunnerPagerAdapter(getContext(), getChildFragmentManager());
        task_list_frg_all = new MyTaskListFragment();
        task_list_frg_wait_Acpt = new MyTaskListFragment();
        task_list_frg_completed = new MyTaskListFragment();
        task_list_frg_out = new MyTaskListFragment();

        runnerPagerAdapter.setItem(getContext().getString(R.string.all_task), task_list_frg_all, null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.not_delivered), task_list_frg_wait_Acpt, null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.task_finished), task_list_frg_completed, null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.task_unavailable), task_list_frg_out, null);

        mytask_view_pager.setAdapter(runnerPagerAdapter);
        mytask_tabs.setViewPager(mytask_view_pager);

        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable(User.class.getName());


        Bundle bundle_all = new Bundle();
        bundle_all.putString("type","all");
        bundle_all.putString("account",user.getAccount());
        task_list_frg_all.setArguments(bundle_all);

        Bundle bundle_wt_acpt = new Bundle();
        bundle_wt_acpt.putString("type","wait_Acpt");
        bundle_wt_acpt.putString("account",user.getAccount());
        task_list_frg_wait_Acpt.setArguments( bundle_wt_acpt );

        Bundle bundle_completed = new Bundle();
        bundle_completed.putString("type","completed");
        bundle_completed.putString("account",user.getAccount());
        task_list_frg_completed.setArguments( bundle_completed );

        Bundle bundle_out = new Bundle();
        bundle_out.putString("type","out");
        bundle_out.putString("account",user.getAccount());
        task_list_frg_out.setArguments( bundle_out );

        return view;
    }


}