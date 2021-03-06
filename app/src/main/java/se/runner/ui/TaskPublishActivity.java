package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.task.Task;
import se.runner.user.User;
import se.runner.widget.RunnerTimePicker;

public class TaskPublishActivity extends AppCompatActivity {
    final public static int TASK_PICK_LOCATION = 0x000A;
    final public static int TASK_DELIVERY_LOCATION = 0x000B;

    final private String TAG = "TaskPublish";

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.publish_location_pick_picker)
    ImageView publish_location_pick_picker;

    @Bind(R.id.publish_location_delivery_picker)
    ImageView publish_location_delivery_picker;

    @Bind(R.id.publish_emergency_picker)
    RatingBar publish_emergency_picker;

    private RunnerTimePicker publishPickTimePicker;
    private RunnerTimePicker publishDeliveryTimePicker;

    private User user;

    private float payment;
    private String category = "玉泉快递";
    private String source_address = "玉泉圆通快递点";
    private String target_address = "玉泉7舍";
    private long gain_time;
    private long delivery_time;
    private int emergency;
    private String consignee;

    private Context context;
    private boolean userBalanceEnough = true;
    private boolean consigneeLegal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(new IconDrawable(this, FontAwesomeIcons.fa_arrow_left).actionBarSize());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        context = this;

        publish_location_pick_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));
        publish_location_delivery_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));

        publishPickTimePicker = new RunnerTimePicker(TaskPublishActivity.this);
        publishDeliveryTimePicker = new RunnerTimePicker(TaskPublishActivity.this);

        publish_emergency_picker.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                if( fromUser ) // change comes from user touch
                {
                    emergency = (int) rating;

                    Log.e(TAG,"current emergency = "+emergency);
                }
            }
        });

        Intent intent = getIntent();
        if( intent != null )
        {
            user = (User) intent.getExtras().getSerializable("user");
            if( user != null)
                Log.e(TAG,"parse account ="+user.getAccount()+" from intent");
            else
            {
                user = new User("null", "null");
                Log.e(TAG,"parse from intent, get nothing");
            }
        }
        else {
            Log.e(TAG, "intent is null");
        }
    }



    @OnClick(R.id.publish_btn_cancel)
    void cancel() {
        finish();
    }

    @OnClick(R.id.publish_btn_confifrm)
    void confirm()
    {
        if( userBalanceEnough == false )
        {
            new AlertDialog.Builder(context)
                    .setTitle("您的账户余额不足")
                    .setMessage("充值后再发布，或者减小赏金")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return ;
        }
        if( consigneeLegal == false )
        {
            new AlertDialog.Builder(context)
                    .setTitle("收货人不合法")
                    .setMessage("快让"+consignee+"注册一个账户吧")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return ;
        }

        Task task = new Task((int)System.currentTimeMillis(),
                user.getAccount(),
                "",
                consignee,
                category,
                System.currentTimeMillis(),
                payment,
                delivery_time,
                gain_time,
                target_address,
                source_address,
                emergency,
                0,
                0,
                0,
                0,
                "null");

        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"server response is null");
                else if( get.equals("") == false )
                {
                    publish_ok();
                }
                else
                {
                    publish_failed();
                }
            }
        };

        task.publish(httpCallback);
    }

    public void publish_ok()
    {
        new AlertDialog.Builder(context)
                .setTitle("任务发布成功！")
                .setMessage("请静候佳音！")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                    }
                })
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void publish_failed()
    {
        new AlertDialog.Builder(context)
                .setTitle("任务发布失败！")
                .setMessage("失败是成功之母~")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                    }
                })
                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick(R.id.publish_category_picker)
    void pickCategory()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择任务类别")
                .setItems( R.array.task_type_list , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        String[] some_array = getResources().getStringArray(R.array.task_type_list);
                        category = some_array[which];
                        Log.e(TAG,"you set category="+category);
                    }
                });
        builder.create();
        builder.show();

    }

    @OnClick(R.id.publish_location_pick_picker)
    void pickPickLocation() {
        Intent intent = new Intent(TaskPublishActivity.this, MapActivity.class);
        startActivityForResult(intent, TASK_PICK_LOCATION);
    }

    @OnClick(R.id.publish_location_delivery_picker)
    void pickDeliveryLocation() {
        Intent intent = new Intent(TaskPublishActivity.this, MapActivity.class);
        startActivityForResult(intent, TASK_DELIVERY_LOCATION);
    }

    @OnClick(R.id.publish_time_pick_picker)
    void pickPickTime()
    {
        publishPickTimePicker.show();
        gain_time = publishPickTimePicker.getCalendar().getTimeInMillis();
    }

    @OnClick(R.id.publish_time_delivery_picker)
    void pickDeliveryTime()
    {
        publishDeliveryTimePicker.show();
        delivery_time = publishDeliveryTimePicker.getCalendar().getTimeInMillis();
    }

    @OnClick(R.id.publish_pay_picker)
    void pickPay()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入赏金");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("我就是这么壕", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payment = Float.parseFloat( input.getText().toString() );
                if( payment > user.getBalance() )
                    userBalanceEnough = false;
                else
                    userBalanceEnough = true;

            }
        });
        builder.setNegativeButton("等等", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @OnClick(R.id.publish_consignee_picker)
    void pickConsignee()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入收货方");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consignee = input.getText().toString() ;

                HttpCallback httpCallback = new HttpCallback()
                {
                    @Override
                    public void onPost(String get)
                    {
                        if( get == null)
                            Log.e(TAG,"server response is null");
                        else if( get.equals("yes"))
                            consigneeLegal = true;
                        else
                            consigneeLegal = false;
                    }
                };
                User.checkUser(consignee ,httpCallback);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        String address;
        switch (requestCode) {
            case TASK_PICK_LOCATION:
                address = data.getStringExtra("address");
                if( address != null && address.length() != 0)
                {
                    Toast.makeText(TaskPublishActivity.this, "设置取货点："+address, Toast.LENGTH_SHORT ).show();
                    source_address = address;
                }
                break;
            case TASK_DELIVERY_LOCATION:
                address = data.getStringExtra("address");
                if( address != null && address.length() != 0)
                {
                    Toast.makeText(TaskPublishActivity.this, "设置收货点："+address, Toast.LENGTH_SHORT ).show();
                    target_address = address;
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
