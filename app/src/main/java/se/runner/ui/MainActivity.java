package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.task.Task;
import se.runner.user.User;


public class MainActivity extends AppCompatActivity
{
    final public static int MAIN_QR_SCAN = 0x000A;

    final private String TAG="Main";

    private User user;
    private Context context;

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

        context = this;

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

        Log.e(TAG,"return to main。requst code = "+ requestCode);
        switch (requestCode)
        {
            case MAIN_QR_SCAN:
                String jsonString = data.getStringExtra(ScanActivity.SCAN_URL);
                Toast.makeText(this,jsonString , Toast.LENGTH_LONG).show();
                handleQRResult(jsonString);
                break;
            case 65536 + UserCenterFragment.CONTACT_LIST:
                Log.e(TAG,"ContactActivity returned");
                String [] tmpList = data.getStringArrayExtra(ContactActivity.LIST_DATA);
                if( tmpList != null && tmpList[0].equals("<Empty-List>") == false )
                {
                    user.setContactList( tmpList );
                    Log.e(TAG,"user contact list updated");
                }

                break;
        }
    }

    public void handleQRResult(String result ) // result is a json string
    {
        if (result == null)
        {
            new AlertDialog.Builder(context)
                    .setTitle("解析二维码失败")
                    .setMessage("您的摄像头近视了")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // continue
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        Task task = Task.parseTaskFromJsonString(result);
        if (task == null)
        {
            new AlertDialog.Builder(context)
                    .setTitle("无效二维码，解析任务失败")
                    .setMessage("您扫的二维码不是本应用需要的二维码")
                    .setPositiveButton("我错了", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // continue
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        if (task.getStatus() == Task.TaskStatus.ACCEPTED)  // task launcher scan for gain cargon, and then task is in progress
        {
            if (user.getAccount().equals(task.getTaskLauncher()) == false) {
                new AlertDialog.Builder(context)
                        .setTitle("无效二维码")
                        .setMessage("您不是本任务的发布者，无权授权他人领取货物，扫描无效")
                        .setPositiveButton("我错了", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // continue
                            }
                        })
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }

            HttpCallback httpCallback = new HttpCallback()
            {
                @Override
                public void onPost(String get)
                {
                    if (get == null)
                        Log.e(TAG, "server response is null");
                    else if (get.equals("success")) {
                        new AlertDialog.Builder(context)
                                .setTitle("成功授权领取货物")
                                .setMessage("您的货物已被送出，正在飞速前往收货人手中")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // continue
                                    }
                                })
                                .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("出了点小问题")
                                .setMessage("要不您再重新扫下二维码？")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // continue
                                    }
                                })
                                .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            };

            task.gaincargo(task.getId(), httpCallback);

        } else if (task.getStatus() == Task.TaskStatus.PROGRESS)  // consignee scan for delivery ok, then task is completed
        {
            if (user.getAccount().equals(task.getTaskConsignee()) == false) {
                new AlertDialog.Builder(context)
                        .setTitle("无效二维码")
                        .setMessage("您不是本任务的收货人，扫描无效")
                        .setPositiveButton("我错了", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // continue
                            }
                        })
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }

            HttpCallback httpCallback = new HttpCallback()
            {
                @Override
                public void onPost(String get)
                {
                    if (get == null)
                        Log.e(TAG, "server response is null");
                    else if (get.equals("success")) {
                        new AlertDialog.Builder(context)
                                .setTitle("收货人成功收货")
                                .setMessage("于是，又一笔任务完成了。")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // continue
                                    }
                                })
                                .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("出了点小问题")
                                .setMessage("要不您再重新扫下二维码？")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // continue
                                    }
                                })
                                .setNegativeButton("Got it", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            };

            task.delivercargo( task.getId(), httpCallback );

        }
    }
}
