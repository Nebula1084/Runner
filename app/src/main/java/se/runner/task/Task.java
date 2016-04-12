package se.runner.task;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;
import se.runner.user.User;

public class Task implements Serializable {
    public enum TaskStatus {
        INIT,   // when created, it is in INIT status. laucher can discard task in this state.
        PUBLISHED,  // when laucher release it to internet(so other can see it), it's in PUBLISHED.  laucher can discard task in this state.
        ACCEPTED,  // when someone accept this task
        PROGRESS,  // when taker(or runner) takes cargo/goods, it's in progress.or task resumed from paused.  laucher/runner can discard task in this state only when other agree.
        ABORT,   // launcher/runner discard task and other agreed.
        PAUSED,  // task is paused, so delay time can be infty, but the task can resume later.
        DELIVERED, // when cargo is delivered
        COMPLETED,  // when task is done,and money transfer is OK. a normal way
        RATED,   // after the task is rated
    }

    final private String TAG = "TASK";
    private int id;
    private String category;
    private double rate;  // [0~5]
    private float payment;
    private int emergency;
    private TaskStatus status;

    // use System.currentTimeMillis() may not be safe, but simple
    private long create_timestamp;

//    private long publish_timestamp;
//    private long accept_timestamp;
//    private long delivery_timestamp;
//    private long deadline_timestamp;
//    private long finish_timestamp;  // finish includes abort or completed

    private long required_gain_time; // time require to fetch the cargo
    private long required_delivery_time;  // time require to finshi delivery
    private long actual_gain_time;
    private long actual_delivery_time;

    private String comment = "null";
    private String description = "null";

    private String taskLauncher;
    private String taskShipper;
    private String taskConsignee;

    private String deliveryAddress;
    private String receivingAddress;

    private transient Context context;

    public Task() {
        category = "category";
        rate = 2;
        payment = 30;
        emergency = 1;
        status = TaskStatus.PROGRESS;
        deliveryAddress = "deliveryAddress";
        receivingAddress = "receivingAddress";
        taskLauncher = "taskLauncher";
        taskShipper = "taskShipper";
        taskShipper = "taskShipper";
    }

    public static Task parseTaskFromJsonString(String jsonString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);

            if( jsonObject != null )
            {

                int tid = (int) jsonObject.get("tid");
                String publisher = jsonObject.getString("publisher");
                String shipper = jsonObject.getString("shipper");
                String consignee = jsonObject.getString("consignee");
                String category = jsonObject.getString("category");
                Long timestamp = (Long) jsonObject.get("timestamp");
                double pay = (double) jsonObject.get("pay");
                int emergency = (int) jsonObject.get("emergency");
                Long delivery_time =  jsonObject.getLong("delivery_time");
                Long recieving_time = jsonObject.getLong("recieving_time");
                String delivery_address = jsonObject.getString("delivery_address");
                String recieving_address = jsonObject.getString("recieving_address");
                int status = (int) jsonObject.get("status");
                Double rate = jsonObject.getDouble("rate");
                Long gain_time = jsonObject.getLong("gain_time");
                Long arrive_time = jsonObject.getLong("arrive_time");
                String comment = jsonObject.getString("comment");

                Task t =  new Task(tid,
                        publisher,
                        shipper,
                        consignee,
                        category,
                        timestamp,
                        (float) pay,
                        emergency,
                        delivery_time,
                        recieving_time,
                        delivery_address,
                        recieving_address,
                        status,
                        rate,
                        gain_time,
                        arrive_time,
                        comment);

                Log.e("JSON",t.toJsonString());

                return t;
            }
        }
        catch( JSONException jex)
        {
            jex.printStackTrace();
        }

        return null;
    }

    public Task(final int tid,
                final String taskLauncherAccount,
                final String taskShipperAccount,
                final String taskConsigneeAccount,
                final String taskCategory,
                final long timestamp,
                final float pay,
                final int emergencyLevel,
                final long delivery_time,
                final long receiving_time,
                final String delivery_address,
                final String receiving_address,
                final int statusCode,
                final double rateCode,
                final long gain_time,
                final long arrive_time,
                final String commentStr) {

        id = tid;
        taskLauncher = taskLauncherAccount;
        taskShipper = taskShipperAccount;
        taskConsignee = taskConsigneeAccount;
        category = taskCategory;
        create_timestamp = timestamp;
        payment = pay;
        deliveryAddress = delivery_address;
        receivingAddress = receiving_address;
        emergency = emergencyLevel;
        rate = rateCode;

        required_gain_time = receiving_time;
        required_delivery_time = delivery_time;

        actual_gain_time = gain_time;
        actual_delivery_time = arrive_time;

        comment = commentStr;

        switch (statusCode)
        {
            case 0: status = TaskStatus.PUBLISHED;break;
            case 1: status = TaskStatus.ACCEPTED;break;
            case 2: status = TaskStatus.PROGRESS;break;
            case 3: status = TaskStatus.DELIVERED;break;
            case 4: status = TaskStatus.COMPLETED;break;
        }
    }

    // no account validation check, may not safe
    // once publish successful, a task will be created.
    public Task(Context ctx,
                final String taskLauncherAccount,
                final String taskCategory,
                final String taskConsigneeAccount,
                final float pay,
                final long delivery_time,
                final long receiving_time,
                final String delivery_address,
                final String receiving_address,
                final int emergencyLevel) {
        context = ctx;

        taskLauncher = taskLauncherAccount;
        category = taskCategory;
        taskConsignee = taskConsigneeAccount;
        payment = pay;

        required_delivery_time = delivery_time;
        required_gain_time = receiving_time;
        deliveryAddress = delivery_address;
        receivingAddress = receiving_address;
        emergency = emergencyLevel;
    }

    public void publish() {
        HttpCallback httpCallback = new HttpCallback() {
            @Override
            public void onPost(String get) {
                if (get == null)
                    Log.e(TAG, "create task return null");
                else if (!get.equals("")) {
                    Log.e(TAG, "publish task successful");
                } else {
                    Log.e(TAG, "publish task failed");
                    new AlertDialog.Builder(context)
                            .setTitle("发布任务失败")
                            .setMessage("我猜多半是网络原因！")
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

        ContentValues para = new ContentValues();
        para.put("account", taskLauncher);
        para.put("consignee", taskConsignee);
        para.put("category", category);
        para.put("pay", payment + "");
        para.put("delivery_time", required_delivery_time + "");
        para.put("receiving_time", required_gain_time + "");
        para.put("delivery_address", deliveryAddress);
        para.put("receiving_address", receivingAddress);
        para.put("emergency", emergency + "");

        new HttpPost("/publish", para, httpCallback).execute();
    }

    public void publish(HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("account", taskLauncher);
        para.put("consignee", taskConsignee);
        para.put("category", category);
        para.put("pay", payment + "");
        para.put("delivery_time", required_delivery_time + "");
        para.put("receiving_time", required_gain_time + "");
        para.put("delivery_address", deliveryAddress);
        para.put("receiving_address", receivingAddress);
        para.put("emergency", emergency + "");

        new HttpPost("/publish", para, httpCallback).execute();
    }

    public static void findTaskByLauncher(String account, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("account", account);

        new HttpPost("/publisherTask", para, httpCallback).execute();
    }

    public static void findTaskByRunner(String account, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("account", account);
        new HttpPost("/runnerTask", para, httpCallback).execute();
    }

    public void findTaskById(int id, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");
        new HttpPost("/gettask", para, httpCallback).execute();
    }

    public static void findAvailableTask(HttpCallback httpCallback) {
        ContentValues para = new ContentValues();

        // TODO: 4/4/16 not sure empty para will make the http post work
        new HttpPost("/availabletask", para, httpCallback).execute();
    }

    public static void accept(String account, int id, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");
        para.put("account", account);

        new HttpPost("/accept", para, httpCallback).execute();
    }

    public void gaincargo(int id, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");

        new HttpPost("/gaincargo", para, httpCallback).execute();
    }



    public void delivercargo(int id, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");

        new HttpPost("/delivercargo", para, httpCallback).execute();
    }

    public void finish(int id, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");

        new HttpPost("/finish", para, httpCallback).execute();
    }

    public void rate(int id, Double rate, String cmt, HttpCallback httpCallback) {
        ContentValues para = new ContentValues();
        para.put("tid", id + "");
        para.put("rate", rate + "");
        para.put("comment", cmt);

        new HttpPost("/rate", para, httpCallback).execute();
    }


    public Task(Context ctx, final String laucherAccountName, final String taskCategory) {
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onPost(String get) {
                if (get == null)
                    Log.e(TAG, "set task launcher return null");
                else if (get.equals("yes")) {
                    taskLauncher = laucherAccountName;

                    category = taskCategory;
                    status = TaskStatus.INIT;
                    create_timestamp = System.currentTimeMillis();
                    id = (int) create_timestamp;

                } else if (get.equals("no")) {
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
        taskShipper = "default_nobody_";
        taskConsignee = "default_nobody_";
        comment = "default_very_good";
        description = "default_this is a tough task";

        User user = new User(context, laucherAccountName, "check existence");
        user.checkUser(callback);
    }

    public void startTask() {
        setStatus(TaskStatus.PROGRESS);
        // TODO: 4/1/16 start task
    }

    public void releaseTask() {
        setStatus(TaskStatus.PUBLISHED);
        // TODO: 4/1/16 release task
    }

    public void resumeTask() {
        setStatus(TaskStatus.PROGRESS);
        // TODO: 4/1/16 resumeTask
    }

    public void pauseTask() {
        setStatus(TaskStatus.PAUSED);
        //// TODO: 4/1/16  pause task
    }

    public void abortTask() {
        setStatus(TaskStatus.ABORT);
        //// TODO: 4/1/16 abort task
    }

    public void comleteTask() {
        setStatus(TaskStatus.COMPLETED);
        //// TODO: 4/1/16 complete task
    }

    public long getCreate_timestamp()
    {
        return create_timestamp;
    }

    public long getActual_gain_time()
    {
        return actual_gain_time;
    }

    public long getActual_delivery_time()
    {
        return actual_delivery_time;
    }

    public int getId() {
        return id;
    }

    public void setDeadline(long t) {
        required_delivery_time = t;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskDescription() {
        return description;
    }

    private void setStatus(TaskStatus t) {
        status = t;
    }

    public void setTaskLauncher(final String accountName) {
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onPost(String get) {
                if (get == null)
                    Log.e(TAG, "set task launcher return null");
                else if (get.equals("yes")) {
                    taskLauncher = accountName;
                } else if (get.equals("no")) {
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

        User user = new User(context, accountName, "check existence");
        user.checkUser(callback);

    }

    public void setTaskShipper(final String accountName) {
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onPost(String get) {
                if (get == null)
                    Log.e(TAG, "set task launcher return null");
                else if (get.equals("yes")) {
                    taskShipper = accountName;
                } else if (get.equals("no")) {
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

        User user = new User(context, accountName, "check existence");
        user.checkUser(callback);
    }

    public void setTaskConsignee(final String accountName) {
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onPost(String get) {
                if (get == null)
                    Log.e(TAG, "set task launcher return null");
                else if (get.equals("yes")) {
                    taskConsignee = accountName;
                } else if (get.equals("no")) {
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

        User user = new User(context, accountName, "check existence");
        user.checkUser(callback);
    }

    public boolean setRate(double rate)
    {
        if (rate < 0 || rate > 5 || status != TaskStatus.COMPLETED)
            return false;

        setStatus(TaskStatus.RATED);
        this.rate = rate;
        return true;
    }

    public double getRate() {
        return rate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        if( comment.equals("") )
            return "null";
        return comment;
    }

    public String getCategory()
    {
        return category;
    }

    public float getPayment()
    {
        return payment;
    }

    public TaskStatus getStatus()
    {
        return status;
    }

    public int getStatusInt()
    {
        int statusCode = 0;

        switch (status)
        {
            case PUBLISHED:statusCode = 0;break;
            case ACCEPTED:statusCode = 1;break;
            case PROGRESS:statusCode = 2;break;
            case DELIVERED:statusCode = 3; break;
            case COMPLETED:statusCode = 4; break;
            default: statusCode = 0;
        }

        return statusCode;

    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public String getTaskLauncher() {
        return taskLauncher;
    }

    public String getTaskShipper() {
        return taskShipper;
    }

    public String getTaskConsignee() {
        return taskConsignee;
    }

    public long getRequired_gain_time()
    {
        return required_gain_time;
    }

    public long getRequired_delivery_time()
    {
        return required_delivery_time;
    }

    public int getEmergency()
    {
        return emergency;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("tid="+id+";");
        builder.append("publisher="+taskLauncher+";");
        builder.append("shipper="+taskShipper+";");
        builder.append("consignee="+taskConsignee+";");
        builder.append("category="+category+";");
        builder.append("timestamp="+create_timestamp+";");
        builder.append("pay="+payment+";");
        builder.append("emergency="+emergency+";");
        builder.append("delivery_time="+required_delivery_time+";");
        builder.append("recieving_time="+required_gain_time+";");
        builder.append("delivery_address="+deliveryAddress+";");
        builder.append("recieving_address="+receivingAddress+";");
        builder.append("status="+status+";");
        builder.append("rate="+rate+";");
        builder.append("gain_time="+actual_gain_time+";");
        builder.append("arrive_time="+actual_delivery_time+";");
        builder.append("comment="+comment+";");

        return builder.toString();
    }

    public String toJsonString()
    {

        StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("\"tid\":"+getId()+",");
        builder.append("\"publisher\":"+getTaskLauncher()+",");
        builder.append("\"shipper\":"+getTaskShipper()+",");
        builder.append("\"consignee\":"+getTaskConsignee()+",");
        builder.append("\"category\":"+getCategory()+",");
        builder.append("\"timestamp\":"+getCreate_timestamp()+",");
        builder.append("\"pay\":"+getPayment()+",");
        builder.append("\"emergency\":"+getEmergency()+",");
        builder.append("\"delivery_time\":"+getRequired_delivery_time()+",");
        builder.append("\"recieving_time\":"+getRequired_gain_time()+",");
        builder.append("\"delivery_address\":"+getDeliveryAddress()+",");
        builder.append("\"recieving_address\":"+getReceivingAddress()+",");
        builder.append("\"status\":"+getStatusInt()+",");
        builder.append("\"rate\":"+getRate()+",");
        builder.append("\"gain_time\":"+getActual_gain_time()+",");
        builder.append("\"arrive_time\":"+getActual_delivery_time()+",");
        builder.append("\"comment\":"+getComment());
        builder.append("}");

        return builder.toString();
    }
}
