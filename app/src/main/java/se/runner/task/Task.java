package se.runner.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;
import se.runner.user.User;

public class Task
{
    enum TaskStatus
    {
        INIT,   // when created, it is in INIT status. laucher can discard task in this state.
        RELEASED,  // when laucher release it to internet(so other can see it), it's in RELEASED.  laucher can discard task in this state.
        PROGRESS,  // when taker(or runner) takes the task, it's in progress.or task resumed from paused.  laucher/runner can discard task in this state only when other agree.
        ABORT,   // launcher/runner discard task and other agreed.
        PAUSED,  // task is paused, so delay time can be infty, but the task can resume later.
        COMPLETED,  // when task is done in a normal way
        RATED,   // after the task is rated
    }

    final private String TAG = "TASK";
    private int id;
    private int type;
    private double rate;  // [0~5]
    private TaskStatus status;

    // use System.currentTimeMillis() may not be safe, but simple
    private long create_timestamp;
    private long release_timestamp;
    private long deadline_timestamp;
    private long finish_timestamp;  // finish includes abort or completed

    private String comment;
    private String description;

    private String taskLauncher;
    private String taskTaker;
    private String taskCustomer;

    private Context context;



    public Task(Context ctx, final String laucherAccountName , final int taskType )
    {
        HttpCallback callback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set task launcher return null");
                else if( get.equals("yes"))
                {
                    taskLauncher = laucherAccountName;

                    type = taskType;
                    status = TaskStatus.INIT;
                    create_timestamp = System.currentTimeMillis();
                    id = (int) create_timestamp;

                }
                else if( get.equals("no"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("创建任务失败")
                            .setMessage("任务创建者账户不合法！")
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
            }
        };


        context = ctx;
        taskLauncher = "default_nobody_";
        taskTaker = "default_nobody_";
        taskCustomer = "default_nobody_";
        comment = "default_very_good";
        description = "default_this is a tough task";

        User user = new User(context,laucherAccountName,"check existence");
        user.checkUser(callback);

    }

    public void startTask()
    {
        setStatus(TaskStatus.PROGRESS);
        // TODO: 4/1/16 start task
    }

    public void releaseTask()
    {
        setStatus(TaskStatus.RELEASED);
        // TODO: 4/1/16 release task
    }

    public void resumeTask()
    {
        setStatus(TaskStatus.PROGRESS);
        // TODO: 4/1/16 resumeTask
    }

    public void pauseTask()
    {
        setStatus(TaskStatus.PAUSED);    
        //// TODO: 4/1/16  pause task
        deadline_timestamp = -1;
    }
    
    public void abortTask()
    {
        setStatus(TaskStatus.ABORT);
        //// TODO: 4/1/16 abort task
    }

    public void comleteTask()
    {
        setStatus(TaskStatus.COMPLETED);
        //// TODO: 4/1/16 complete task
    }

    public int getId()
    {
        return id;
    }

    public void setDeadline(long t)
    {
        deadline_timestamp = t;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTaskDescription()
    {
        return description;
    }

    private void setStatus(TaskStatus t)
    {
        status = t;
    }

    public void setTaskLauncher(final String accountName)
    {
        HttpCallback callback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set task launcher return null");
                else if( get.equals("yes"))
                {
                    taskLauncher = accountName;
                }
                else if( get.equals("no"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("更改任务发布者失败")
                            .setMessage("任务发起者账户不合法！")
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
            }
        };

        User user = new User(context,accountName,"check existence");
        user.checkUser(callback);

    }

    public void setTaskTaker(final String accountName )
    {
        HttpCallback callback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set task launcher return null");
                else if( get.equals("yes"))
                {
                    taskTaker = accountName;
                }
                else if( get.equals("no"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("承接任务失败")
                            .setMessage("任务承接者账户不合法！")
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
            }
        };

        User user = new User(context,accountName,"check existence");
        user.checkUser(callback);
    }

    public void setTaskCustomer(final String accountName )
    {
        HttpCallback callback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set task launcher return null");
                else if( get.equals("yes"))
                {
                    taskCustomer = accountName;
                }
                else if( get.equals("no"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("设定任务客户失败")
                            .setMessage("任务客户账户不合法！")
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
            }
        };

        User user = new User(context,accountName,"check existence");
        user.checkUser(callback);
    }

    public boolean setRate(double rate)
    {
        if( rate <0 || rate > 5 || status!= TaskStatus.COMPLETED )
            return false;

        setStatus(TaskStatus.RATED);
        this.rate = rate;
        return true;
    }

    public double getRate()
    {
        return rate;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment()
    {
        return comment;
    }

}
