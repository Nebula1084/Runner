package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.task.Task;

public class TaskAcceptActivity extends AppCompatActivity {
    final private String TAG = "TaskAccept";

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.accept_location_pick)
    TextView accept_location_pick;

    @Bind(R.id.accept_time_pick)
    TextView accept_time_pick;

    @Bind(R.id.accept_location_delivery)
    TextView accept_location_delivery;

    @Bind(R.id.accept_time_delivery)
    TextView accept_time_delivery;

    @Bind(R.id.accept_category)
    TextView accetp_category;

    @Bind(R.id.accept_emergency)
    RatingBar accept_emergency;

    @Bind(R.id.accept_pay)
    TextView accept_pay;

    @Bind(R.id.accept_consignee)
    TextView accept_consignee;

    private Context context;
    Task task;
    private String account;
//    int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        context = this;
        Intent intent = getIntent();
//        taskId = (int) intent.getSerializableExtra(Task.class.getName());

//        task = new Task();

        if( intent != null )
        {
            task = (Task) intent.getSerializableExtra(Task.class.getName());
            account = intent.getStringExtra("account");
            Log.e(TAG,"get account= "+account);
        }
        else
        {
            Log.e(TAG,"intetn is null");
            task = new Task();
            account = "null";
        }


        accept_location_pick.setText(task.getDeliveryAddress());
        accept_location_delivery.setText(task.getReceivingAddress());
        accept_consignee.setText(task.getTaskConsignee());
        accept_pay.setText(task.getPayment()+"");
        accetp_category.setText(task.getCategory());
        accept_time_pick.setText(task.getRequired_gain_time()+"");
        accept_time_delivery.setText(task.getRequired_delivery_time()+"");
        accept_emergency.setRating(task.getEmergency());

    }



    @OnClick(R.id.accept_btn_confifrm)
    void accept()
    {
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null)
                    Log.e(TAG,"server response is null");
                else if( get.equals("success"))
                {
                    confirm_accept();
                }
                else
                {
                    accept_failed();
                }
            }
        };

        Task.accept(account,task.getId(),httpCallback);
    }

    public void confirm_accept()
    {
        new AlertDialog.Builder(context)
                .setTitle("抢单成功！")
                .setMessage("用户"+account+"又可以大赚一笔！")
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

    public void accept_failed()
    {
        new AlertDialog.Builder(context)
                .setTitle("抢单失败！")
                .setMessage("别慌！多试几次~")
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}