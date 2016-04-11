package se.runner.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.runner.R;
import se.runner.task.Task;
import se.runner.widget.TaskListFragment;

public class MyDeliveryListFragment extends TaskListFragment implements TaskListFragment.OnRefreshListener, AdapterView.OnItemClickListener {
    public MyDeliveryListFragment() {
        super();
        setItemRsId(R.layout.item_task_small);
        setOnRefreshListener(this);
        setOnItemClickListener(this);
        /*for test*/
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());
        setTasks(tasks);
        /*until here*/
    }

    @Override
    public void onRefresh() {
        /*for test. add a task to the list view*/
        Toast.makeText(getContext(), "TaskSquare", Toast.LENGTH_LONG).show();
        addTask(new Task());
        update();// and notify the adapter to update the listview
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getContext(), MyDeliveryActivity.class));
    }
}
