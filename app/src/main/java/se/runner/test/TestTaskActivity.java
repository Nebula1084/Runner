package se.runner.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import se.runner.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.request.HttpCallback;
import se.runner.task.Task;


public class TestTaskActivity extends AppCompatActivity
{
    final private String TAG = "test task";
    private Task task;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_task);
        ButterKnife.bind(this);
        context = this;

        task = new Task(this,"android","test","server",10,12345,67890,"hangzhou","beijing",1);
    }

    public void showResult(String response)
    {
        new AlertDialog.Builder(context)
                .setTitle("info")
                .setMessage(response)
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

    @OnClick(R.id.publishBtn)
    public void publish()
    {
        Log.e(TAG,"you click publish button");

        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( !get.equals("") )
                {
                    Log.e(TAG, "publish task successful");
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "publish task failed");
                }
            }
        };

        task.publish(httpCallback);
    }

    @OnClick(R.id.getTaskBtn)
    public void gettask()
    {
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( !get.equals("") )
                {
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "get task failed");
                }
            }
        };

        String tid = ((EditText)findViewById(R.id.editText_gettask)).getText().toString();
        task.findTaskById(Integer.parseInt(tid), httpCallback);
    }

    @OnClick(R.id.availableBtn)
    public void availabletask()
    {
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( !get.equals("") )
                {
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "available task failed");
                }
            }
        };

        task.findAvailableTask(httpCallback);
    }

    @OnClick(R.id.runnerTaskBtn)
    public void find_task_by_runner()
    {
        String account = ((EditText)findViewById(R.id.editText_runnertask)).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( !get.equals("") )
                {
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "find runner task failed");
                }
            }
        };
        Log.e(TAG,"from editText_runnertask:"+account);
        task.findTaskByRunner(account, httpCallback);
    }

    @OnClick(R.id.publisherTaskBtn)
    public void find_task_by_publisher()
    {
        String account = ((EditText) findViewById(R.id.editText_publishertask) ).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( !get.equals("") )
                {
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "find publisher's task failed");
                }
            }
        };
        Log.e(TAG,"from editText_publishertask:"+account);
        task.findTaskByLauncher(account, httpCallback);
    }

    @OnClick(R.id.acceptBtn)
    public void accept()
    {
        String tid = ((EditText)findViewById(R.id.editText_accept)).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( get.equals("success") )
                {
                    Log.e(TAG, "accept account ok");
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "accept task failed");
                }
            }
        };

        task.accept("android", Integer.parseInt(tid), httpCallback);
    }

    @OnClick(R.id.rateBtn)
    public void rate()
    {
        String tid = ((EditText)findViewById(R.id.editText_rate)).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "create task return null");
                else if( get.equals("success") )
                {
                    Log.e(TAG,"rate task ok");
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "rate task failed");
                }
            }
        };

        task.rate(Integer.parseInt(tid), 5, "a good task", httpCallback);
    }

    @OnClick(R.id.gaincargoBtn)
    public void gaincargo()
    {
        String tid = ( (EditText) findViewById(R.id.editText_gaincargo)).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "delivercargo task return null");
                else if( get.equals("success") )
                {
                    Log.e(TAG,"gaincargo task ok");
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "gaincargo task failed");
                }
            }
        };

        task.gaincargo(Integer.parseInt(tid), httpCallback);
    }

    @OnClick(R.id.delivercargoBtn)
    public void delivercargo()
    {
        String tid = ( (EditText) findViewById(R.id.editText_delivercargo)).getText().toString();
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )
                    Log.e(TAG, "delivercargo task return null");
                else if( get.equals("success") )
                {
                    Log.e(TAG,"delivercargo task ok");
                    showResult(get);
                }
                else
                {
                    Log.e(TAG, "delivercargo task failed");
                }
            }
        };

        task.delivercargo(Integer.parseInt(tid),httpCallback);
    }
}
