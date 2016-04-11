package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import se.runner.R;
import se.runner.task.Task;

public class TaskStatusFragment extends Fragment implements PtrHandler {
    private PtrFrameLayout task_status_ptr_frame;
    private LinearLayout task_status_queue;
    private int count = 0;
    private Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_status, container, false);
        task_status_ptr_frame = (PtrFrameLayout) view.findViewById(R.id.task_status_ptr_frame);
        task_status_queue = (LinearLayout) view.findViewById(R.id.task_status_queue);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        task_status_ptr_frame.setHeaderView(header);
        task_status_ptr_frame.setPtrHandler(this);
        task = (Task) getArguments().getSerializable(Task.class.getName());
        return view;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        count++;
        if (count % 2 != 0) {
            ptrFrameLayout.refreshComplete();
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);
            TextView textView = (TextView) linearLayout.getChildAt(linearLayout.getChildCount() - 1);
            textView.setText(task.getReceivingAddress() + count);
        } else {
            ptrFrameLayout.refreshComplete();
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_task_finished, task_status_queue);
            ButtonRectangle buttonRectangle = (ButtonRectangle) linearLayout.getChildAt(linearLayout.getChildCount() - 1);
            buttonRectangle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CommentActivity.class);
                    intent.putExtra(Task.class.getName(), task);
                    startActivity(intent);
                }
            });
        }

    }
}
