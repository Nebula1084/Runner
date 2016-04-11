package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.task.Task;

public class MyTaskActivity extends AppCompatActivity {
    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.mytask_tabs)
    MaterialTabs mytask_tabs;

    @Bind(R.id.mytask_view_pager)
    ViewPager mytask_view_pager;

    RunnerPagerAdapter runnerPagerAdapter;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytask);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        runnerPagerAdapter = new RunnerPagerAdapter(MyTaskActivity.this, getSupportFragmentManager());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra(Task.class.getName());

        TaskStatusFragment taskStatusFragment = new TaskStatusFragment();
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Task.class.getName(), task);
        taskStatusFragment.setArguments(bundle);
        runnerPagerAdapter.setItem("任务状态", taskStatusFragment, null);
        taskDetailFragment.setArguments(bundle);
        runnerPagerAdapter.setItem("任务详情", taskDetailFragment, null);
        mytask_view_pager.setAdapter(runnerPagerAdapter);
        mytask_tabs.setViewPager(mytask_view_pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}