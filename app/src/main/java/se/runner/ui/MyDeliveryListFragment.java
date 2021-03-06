package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.task.Task;
import se.runner.widget.TaskListFragment;

public class MyDeliveryListFragment extends TaskListFragment implements TaskListFragment.OnRefreshListener, AdapterView.OnItemClickListener {
   final static private String TAG = "DeliveryList";

    private String type = "null";
    private String account = "null";

    public MyDeliveryListFragment() {
        super();
        setItemRsId(R.layout.item_task_small);
        setOnRefreshListener(this);
        setOnItemClickListener(this);

        Bundle bundle = getArguments();
        if( bundle != null )
        {
            type = bundle.getString("type");
            account = bundle.getString("account");
        }
        else
            Log.e(TAG,"bundle is null");

        refreshTasks();

    }

    public void refreshTasks()
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
                    parseTasksFromResponse(get);
                }
                else
                {
                    Log.e(TAG, "available task failed");
                }
            }
        };

        Task.findTaskByRunner(account,httpCallback);
    }

    public void parseTasksFromResponse(String response)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(response);
            List<Task> taskList = new ArrayList<>();

            Log.e(TAG,"json array has "+jsonArray.length()+" objects");
            for (int i=0; i< jsonArray.length(); i++ )
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if( jsonObject != null )
                {
                    // can't cast to long and float
                    int status = (int) jsonObject.get("status");

                    if ( type.equals("progress") && status != 2 )
                        continue;
                    else if( type.equals("completed") && status < 3 )
                        continue;


                    int tid = (int) jsonObject.get("tid");
                    String publisher = jsonObject.getString("publisher");
                    String shipper = jsonObject.getString("shipper");
                    String consignee = jsonObject.getString("consignee");
                    String category = jsonObject.getString("category");
                    Long timestamp = (Long) jsonObject.get("timestamp");
                    double pay = (double) jsonObject.get("pay");
                    int emergency = (int) jsonObject.get("emergency");
                    Long delivery_time = jsonObject.getLong("delivery_time");
                    Long recieving_time = jsonObject.getLong("recieving_time");
                    String delivery_address = jsonObject.getString("delivery_address");
                    String recieving_address = jsonObject.getString("recieving_address");

                    Double rate = jsonObject.getDouble("rate");
                    Long gain_time = jsonObject.getLong("gain_time");
                    Long arrive_time = jsonObject.getLong("arrive_time");
                    String comment = jsonObject.getString("comment");

                    Task task = new Task(tid,
                            publisher,
                            shipper,
                            consignee,
                            category,
                            timestamp,
                            (float)pay,
                            delivery_time,
                            recieving_time,
                            delivery_address,
                            recieving_address,
                            emergency,
                            status,
                            rate,
                            gain_time,
                            arrive_time,
                            comment);

                    if( task != null )
                    {
                        Log.e(TAG,task.toString());
                        taskList.add(task);
                    }
                    else
                        Log.e(TAG,"task list parser--> task is null");
                }
            }

            if( taskList != null)
            {
                Log.e(TAG,"task list has "+taskList.size() + " tasks");
                setTasks(taskList);

                update();
            }
        }
        catch (JSONException jex)
        {
            jex.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        /*for test. add a task to the list view*/
        Toast.makeText(getContext(), "刷呀刷呀...", Toast.LENGTH_SHORT ).show();
//        addTask(new Task());
//        update();// and notify the adapter to update the listview

        if( type.equals("null") || account.equals("null"))
        {
            Bundle bundle = getArguments();
            if( bundle != null )
            {
                type = bundle.getString("type");
                account = bundle.getString("account");
            }
            else
                Log.e(TAG,"bundle is null");
        }


        refreshTasks();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), MyDeliveryActivity.class);
        intent.putExtra(Task.class.getName(), (Task) parent.getAdapter().getItem(position));
        startActivity(intent);
    }
}
