package se.runner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.user.User;
import se.runner.widget.TaskListFragment;

public class MyDeliveryFragment extends Fragment {

    private MaterialTabs mydelivery_tabs;
    private ViewPager mydelivery_view_pager;

    private RunnerPagerAdapter runnerPagerAdapter;

    private User user;
    private MyDeliveryListFragment delivery_list_frg_all;
    private MyDeliveryListFragment delivery_list_frg_progress;
    private MyDeliveryListFragment delivery_list_frg_completed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydelivery, container, false);
        mydelivery_tabs = (MaterialTabs) view.findViewById(R.id.mydelivery_tabs);
        mydelivery_view_pager = (ViewPager) view.findViewById(R.id.mydelivery_view_pager);

        runnerPagerAdapter = new RunnerPagerAdapter(getContext(), getChildFragmentManager());

        delivery_list_frg_all = new MyDeliveryListFragment();
        delivery_list_frg_progress = new MyDeliveryListFragment();
        delivery_list_frg_completed = new MyDeliveryListFragment();

        runnerPagerAdapter.setItem(getContext().getString(R.string.all_delivey), delivery_list_frg_all, null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.deliverying), delivery_list_frg_progress, null);
        runnerPagerAdapter.setItem(getContext().getString(R.string.deliveryed), delivery_list_frg_completed, null);
        mydelivery_view_pager.setAdapter(runnerPagerAdapter);
        mydelivery_tabs.setViewPager(mydelivery_view_pager);

        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable(User.class.getName());

//        user = new User(bundle.getString("account"),bundle.getString("passwd"));
//        user.login();


        Bundle bundle_all = new Bundle();
        bundle_all.putString("account",user.getAccount());
        bundle_all.putString("type","all");
        delivery_list_frg_all.setArguments(bundle_all);

        Bundle bundle_progress = new Bundle();
        bundle_progress.putString("account",user.getAccount());
        bundle_progress.putString("type","progress");
        delivery_list_frg_progress.setArguments(bundle_progress);

        Bundle bundle_completed = new Bundle();
        bundle_completed.putString("account",user.getAccount());
        bundle_completed.putString("type","completed");
        delivery_list_frg_completed.setArguments(bundle_completed);

        return view;
    }
}