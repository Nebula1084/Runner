package se.runner.task;

import android.content.Context;

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


    public Task(Context ctx, String laucherAccountName , int taskType )
    {
        if( checkAccountExistence(laucherAccountName) == false )
            return;

        context = ctx;
        taskLauncher = laucherAccountName;
        taskTaker = "undefined";
        taskCustomer = "undefined";

        type = taskType;
        status = TaskStatus.INIT;
        create_timestamp = System.currentTimeMillis();
        id = (int) create_timestamp;
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

    public boolean setTaskLauncher(String accountName)
    {
        if( checkAccountExistence(accountName) == false )
            return false;
        taskLauncher = accountName;
        return true;
    }

    public boolean setTaskTaker(String accountName )
    {
        if( checkAccountExistence(accountName) == false )
            return false;
        taskTaker = accountName;
        return true;
    }

    public boolean setTaskCustomer(String accountName )
    {
        if( checkAccountExistence(accountName) == false )
            return false;
        taskCustomer = accountName;
        return true;
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

    public boolean checkAccountExistence(String accountName )
    {
        User user = new User(context,accountName,"testExistence");
        return user.isRegistered();
    }
}
