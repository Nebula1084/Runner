package se.runner.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;
import se.runner.R;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_tabs)
    MaterialTabs main_tabs;

    @Bind(R.id.main_view_pager)
    ViewPager main_view_pager;

    RunnerPagerAdapter runnerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        runnerPagerAdapter = new RunnerPagerAdapter(getSupportFragmentManager());
        runnerPagerAdapter.setItem(getString(R.string.task_square), new TaskSquareFragment());
        runnerPagerAdapter.setItem(getString(R.string.mydelivery), new MyDeliveryFragment());
        runnerPagerAdapter.setItem(getString(R.string.mytask), new MyTaskFragment());
        runnerPagerAdapter.setItem(getString(R.string.user_center), new UserCenterFragment());
        main_view_pager.setAdapter(runnerPagerAdapter);
        main_tabs.setViewPager(main_view_pager);
    }

}
