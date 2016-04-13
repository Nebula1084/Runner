package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;
import se.runner.task.Task;
import se.runner.user.User;
import se.runner.widget.TaskListFragment;

public class TaskSquareFragment extends TaskListFragment implements TaskListFragment.OnRefreshListener, AdapterView.OnItemClickListener {
    final private static String TAG = "TaskSquare";
    private User user;

    private List<Task> tasks = new ArrayList<>();

    public TaskSquareFragment()
    {
        super();
        setItemRsId(R.layout.item_task_large);
        setOnRefreshListener(this);
        setOnItemClickListener(this);


    refreshTasks();

//        /*for test*/
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(new Task());
//        tasks.add(new Task());
//        setTasks(tasks);
//        /*until here*/
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

        Task.findAvailableTask(httpCallback);
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
                    int tid = (int) jsonObject.get("tid");
                    String publisher = jsonObject.getString("publisher");
                    String shipper = jsonObject.getString("shipper");
                    String consignee = jsonObject.getString("consignee");
                    String category = jsonObject.getString("category");
                    Long timestamp =  jsonObject.getLong("timestamp");
                    double pay = (double) jsonObject.get("pay");
                    int emergency = (int) jsonObject.get("emergency");
                    Long delivery_time = jsonObject.getLong("delivery_time");
                    Long recieving_time = jsonObject.getLong("recieving_time");
                    String delivery_address = jsonObject.getString("delivery_address");
                    String recieving_address = jsonObject.getString("recieving_address");
                    int status = (int) jsonObject.get("status");
                    Double rate =  jsonObject.getDouble("rate");
                    Long gain_time =  jsonObject.getLong("gain_time");
                    Long arrive_time = jsonObject.getLong("arrive_time");
                    String comment = jsonObject.getString("comment");

                    // disgusting
                    // but jsonobject can return integer or long depends
//                    Long timestamp_l;
//                    if( timestamp instanceof Integer)
//                        timestamp_l = new Long((Integer)timestamp);
//                    else
//                        timestamp_l = (Long) timestamp;
//
//                    Long delivery_time_l;
//                    if( delivery_time instanceof Integer)
//                        delivery_time_l = new Long((Integer) delivery_time);
//                    else
//                        delivery_time_l = (Long) delivery_time;


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

                tasks = taskList;

                update();
            }
        }
        catch (JSONException jex)
        {
            jex.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        Bundle bundle = getArguments();
        if( bundle != null ) {
            user = (User) bundle.getSerializable(User.class.getName());
            Log.e(TAG,"user account "+user.getAccount());
        }
        else
            Log.e(TAG,"Bundle is null");

        return view;
    }

    @Override
    public void onRefresh()
    {
        /*for test. add a task to the list view*/
        Toast.makeText(getContext(), "正在刷新...", Toast.LENGTH_SHORT ).show();

        refreshTasks();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getContext(), TaskAcceptActivity.class);
        int taskId = ((Task) parent.getAdapter().getItem(position)).getId();
//        intent.putExtra(Task.class.getName() , taskId );
        intent.putExtra(Task.class.getName() , (Task) parent.getAdapter().getItem(position) );
        intent.putExtra("account",user.getAccount());

        startActivity(intent);
    }
}
