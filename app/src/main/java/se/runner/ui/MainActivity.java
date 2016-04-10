package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;
import se.runner.R;


public class MainActivity extends AppCompatActivity {
    final public static int MAIN_QR_SCAN = 0x000A;

    @Bind(R.id.main_tabs)
    MaterialTabs main_tabs;

    @Bind(R.id.main_view_pager)
    ViewPager main_view_pager;

    @Bind(R.id.tool_bar)
    Toolbar tool_bar;

    RunnerPagerAdapter runnerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);

        runnerPagerAdapter = new RunnerPagerAdapter(MainActivity.this, getSupportFragmentManager());
        runnerPagerAdapter.setItem(getString(R.string.task_square), new TaskSquareFragment(), new IconDrawable(this, FontAwesomeIcons.fa_search));
        runnerPagerAdapter.setItem(getString(R.string.mydelivery), new MyDeliveryFragment(), new IconDrawable(this, FontAwesomeIcons.fa_shopping_bag));
        runnerPagerAdapter.setItem(getString(R.string.mytask), new MyTaskFragment(), new IconDrawable(this, FontAwesomeIcons.fa_tasks));
        runnerPagerAdapter.setItem(getString(R.string.user_center), new UserCenterFragment(), new IconDrawable(this, FontAwesomeIcons.fa_user));
        main_view_pager.setAdapter(runnerPagerAdapter);
        main_tabs.setViewPager(main_view_pager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search).actionBarSize());
        menu.getItem(1).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus).actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_publish:
                startActivity(new Intent(MainActivity.this, TaskPublishActivity.class));
                break;
            case R.id.action_scan:
                startActivityForResult(new Intent(MainActivity.this, ScanActivity.class), MAIN_QR_SCAN);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case MAIN_QR_SCAN:
                Toast.makeText(this, data.getStringExtra(ScanActivity.SCAN_URL), Toast.LENGTH_LONG).show();
                break;
        }
    }

}
