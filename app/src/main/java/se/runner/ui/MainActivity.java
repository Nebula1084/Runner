package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.user.User;
import se.runner.widget.CaptureActivityAnyOrientation;


public class MainActivity extends AppCompatActivity
{
    final public static int MAIN_QR_SCAN = 0x000A;
    final private String TAG="Main";

    private User user;

    private TaskSquareFragment taskSquareFragment;
    private MyDeliveryFragment myDeliveryFragment;
    private MyTaskFragment myTaskFragment;
    private UserCenterFragment userCenterFragment;

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

        taskSquareFragment = new TaskSquareFragment();
        myDeliveryFragment = new MyDeliveryFragment();
        myTaskFragment = new MyTaskFragment();
        userCenterFragment = new UserCenterFragment();

        runnerPagerAdapter = new RunnerPagerAdapter(MainActivity.this, getSupportFragmentManager());

//        runnerPagerAdapter.setItem(getString(R.string.task_square), new TaskSquareFragment(), new IconDrawable(this, FontAwesomeIcons.fa_search));
//        runnerPagerAdapter.setItem(getString(R.string.mydelivery), new MyDeliveryFragment(), new IconDrawable(this, FontAwesomeIcons.fa_shopping_bag));
//        runnerPagerAdapter.setItem(getString(R.string.mytask), new MyTaskFragment(), new IconDrawable(this, FontAwesomeIcons.fa_tasks));
//        runnerPagerAdapter.setItem(getString(R.string.user_center), new UserCenterFragment(), new IconDrawable(this, FontAwesomeIcons.fa_user));
        runnerPagerAdapter.setItem("任务广场", taskSquareFragment, new IconDrawable(this, FontAwesomeIcons.fa_search));
        runnerPagerAdapter.setItem(getString(R.string.mydelivery), myDeliveryFragment, new IconDrawable(this, FontAwesomeIcons.fa_shopping_bag));
        runnerPagerAdapter.setItem(getString(R.string.mytask), myTaskFragment, new IconDrawable(this, FontAwesomeIcons.fa_tasks));
        runnerPagerAdapter.setItem(getString(R.string.user_center), userCenterFragment, new IconDrawable(this, FontAwesomeIcons.fa_user));

        main_view_pager.setAdapter(runnerPagerAdapter);
        main_tabs.setViewPager(main_view_pager);

        Intent intent = getIntent();
        if( intent != null )
        {
            user = new User(this, intent.getExtras().getString("account") , intent.getExtras().getString("passwd") );
        }
        else
        {
            Log.e(TAG,"intent is null");
            user =new User("null","null");
        }
        user.login();

        Bundle bundle = new Bundle();
        bundle.putSerializable(User.class.getName(),user);

//        bundle.putString("account",user.getAccount());
//        bundle.putString("account",user.getPasswd());

        userCenterFragment.setArguments(bundle);
        myTaskFragment.setArguments(bundle);
        myDeliveryFragment.setArguments(bundle);
        taskSquareFragment.setArguments(bundle);

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
                Intent intent = new Intent(MainActivity.this, TaskPublishActivity.class);
                intent.putExtra("account",user.getAccount());
                startActivity(intent);
                break;
            case R.id.action_scan:
                startActivityForResult(new Intent(MainActivity.this, ScanActivity.class), MAIN_QR_SCAN);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
