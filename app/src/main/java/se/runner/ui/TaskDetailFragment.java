package se.runner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.runner.R;
import se.runner.task.Task;

public class TaskDetailFragment extends Fragment {
    Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        task = (Task) getArguments().getSerializable(Task.class.getName());

        return view;
    }
}
