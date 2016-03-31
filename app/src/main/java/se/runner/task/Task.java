package se.runner.task;

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
    private User taskLaucher;
    private User taskTaker;
    private User taskCustomer;


    public Task(User laucher, int taskType )
    {
        if (laucher.isRegistered() == false )
        {
            //// TODO: 4/1/16 handle unregistered user task request

            return ;
        }
        status = TaskStatus.INIT;
        create_timestamp = System.currentTimeMillis();
    }

    public void gainCargo()
    {

    }

    public void deliverCargo()
    {

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

    private void setStatus(TaskStatus t)
    {
        status = t;
    }

    public void setTaskLaucher(User user)
    {
        taskLaucher = user;
    }

    public void setTaskTaker(User user)
    {
        taskTaker = user;
    }

    public void setTaskCustomer(User user)
    {
        taskCustomer = user;
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

    }

    public String getComment()
    {
        return comment;
    }
}
