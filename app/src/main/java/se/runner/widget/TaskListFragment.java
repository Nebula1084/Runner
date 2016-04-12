package se.runner.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import se.runner.R;
import se.runner.task.Task;
import se.runner.user.User;


public class TaskListFragment extends Fragment implements PtrHandler
{
    private final static String TAG = "TaskListFragment";

    private ListView task_list;
    private TaskListAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private PtrFrameLayout task_list_ptr_frame;
    private int itemRsId;
    private OnRefreshListener onRefreshListener;
    List<Task> tasks;



    public interface OnRefreshListener {
        void onRefresh();
    }

    public TaskListFragment() {
        super();
    }

    public static TaskListFragment newInstance(int itemRsId) {
        TaskListFragment instance = new TaskListFragment();
        instance.setItemRsId(itemRsId);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        task_list = (ListView) view.findViewById(R.id.task_list);
        task_list_ptr_frame = (PtrFrameLayout) view.findViewById(R.id.task_list_ptr_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        task_list_ptr_frame.setHeaderView(header);
        task_list_ptr_frame.setPtrHandler(this);
        if (adapter == null)
            setAdapter(new TaskListAdapter(getContext(), itemRsId));
        task_list.setAdapter(adapter);
        if (tasks != null)
            adapter.setTasks(tasks);
        if (onItemClickListener != null)
            task_list.setOnItemClickListener(onItemClickListener);





        return view;
    }

    public void setAdapter(TaskListAdapter adapter)
    {
        if( adapter == null )
            return ;

        this.adapter = adapter;
        update();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setTasks(List<Task> tasks)
    {
        this.tasks = tasks;
        if( adapter != null ) {
            adapter.setTasks(this.tasks);
            update();
        }
    }

    public void addTask(Task task)
    {
        adapter.addTask(task);
        update();
    }

    public void removeTask(Task task) {
        adapter.removeTask(task);
        update();
    }

    public void removeTask(int index)
    {
        adapter.removeTask(index);
        update();
    }

    public void update() {
        if( adapter != null )
            adapter.notifyDataSetChanged();
        else
            Log.e(TAG,"adapter is null");
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setItemRsId(int itemRsId) {
        this.itemRsId = itemRsId;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout)
    {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
        ptrFrameLayout.refreshComplete();
    }
}
