package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

import java.util.Calendar;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import se.runner.R;
import se.runner.task.Task;

public class TaskStatusFragment extends Fragment implements PtrHandler
{
    private final String TAG = "TaskStatus";

    private PtrFrameLayout task_status_ptr_frame;
    private LinearLayout task_status_queue;
    private Task task;

    private boolean isAdd_publish_status = false;
    private boolean isAdd_accept_status = false;
    private boolean isAdd_progress_status = false;
    private boolean isAdd_delivered_status = false;
    private boolean isAdd_completed_status = false;
    private boolean isAdd_comment_status = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_status, container, false);
        task_status_ptr_frame = (PtrFrameLayout) view.findViewById(R.id.task_status_ptr_frame);
        task_status_queue = (LinearLayout) view.findViewById(R.id.task_status_queue);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        task_status_ptr_frame.setHeaderView(header);
        task_status_ptr_frame.setPtrHandler(this);
        task = (Task) getArguments().getSerializable(Task.class.getName());
        if( task == null)
        {
            task = new Task();
            Log.e(TAG,"task is null");
        }
        return view;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

        int task_status = parse_task_status(task.getStatus());

        for(int i=0;i<task_status; i++)
        {
            add_status( i );
        }



    }

    public String milles_to_chinese_format(long time_in_mills)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( time_in_mills );
        return calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日"+calendar.get(Calendar.HOUR_OF_DAY)+"时"+calendar.get(Calendar.MINUTE)+"分"+calendar.get(Calendar.SECOND)+"秒";
    }

    public int parse_task_status(Task.TaskStatus s)
    {
        int task_status = 0;
        switch (s)
        {
            case INIT:task_status = 0;break;
            case PUBLISHED:task_status = 1; break;
            case ACCEPTED:task_status = 2;break;
            case PROGRESS:task_status = 3;break;
            case DELIVERED:task_status = 4;break;
            case COMPLETED:task_status = 5;break;
            case RATED:task_status = 6;break;
            default:task_status = 0;break;
        }
        return task_status;
    }

    public void add_status( int code)
    {
        switch (code)
        {
            case 0: add_publish_status();break;
            case 1: add_accept_status();break;
            case 2: add_progress_status();break;
            case 3: add_delivered_status();break;
            case 4: add_completed_status();break;
            case 5: add_comment_status();break;
            default: break;
        }
    }

    public void add_publish_status( )
    {
        if( isAdd_publish_status )
            return;

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("已发布,等待被抢单\n"+ milles_to_chinese_format(task.getCreate_timestamp() ));
        isAdd_publish_status = true;
    }

    public void add_accept_status( )
    {
        if( isAdd_accept_status )
            return;

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("已被抢单，等待接货开始任务\n"+ milles_to_chinese_format( task.getActual_gain_time() ));
        isAdd_accept_status = true;
    }

    public void add_progress_status()
    {
        if( isAdd_progress_status )
            return;

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("任务进行中\n"+ "执行者:"+task.getTaskShipper() );
        isAdd_progress_status = true;
    }

    public void add_delivered_status()
    {
        if( isAdd_delivered_status )
            return ;

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("货物已送达\n"+ milles_to_chinese_format( task.getActual_delivery_time() ));
        isAdd_delivered_status = true;
    }

    public void add_completed_status(  )
    {
        if( isAdd_completed_status )
            return ;
        isAdd_completed_status = true;

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("已付款，等待评价\n"+ milles_to_chinese_format( task.getActual_delivery_time() ));


        LinearLayout linearLayout2 = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_task_finished, task_status_queue);

        ButtonRectangle buttonRectangle = (ButtonRectangle) linearLayout2.getChildAt(linearLayout.getChildCount() - 1);
        buttonRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra(Task.class.getName(), task);
                startActivity(intent);
            }
        });
    }

    public void add_comment_status( )
    {
        if( isAdd_comment_status )
            return ;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_message, task_status_queue);

        TextView textview = (TextView) linearLayout.getChildAt( linearLayout.getChildCount() - 1 );
        textview.setText("已评价。\n评分="+task.getRate()+"\n"+task.getComment()+"\n快去发布新任务吧");
        isAdd_comment_status = true;
    }
}
