package se.runner.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import se.runner.R;
import se.runner.task.Task;

public class TaskListAdapter extends BaseAdapter {
    private final static String TAG = "TaskAdapter";
    private Context context;
    private int resId;
    private List<Task> tasks;


    public TaskListAdapter(Context context, int resId) {
        this.context = context;
        this.resId = resId;
        tasks = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

//        Log.e(TAG,"You are in TaskListAdapter");

        switch (resId)
        {
            case R.layout.item_task_large:
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(context).inflate(resId, null);
                    holder = new ViewHolder();

                    holder.task_item_distance = (ImageView) convertView.findViewById(R.id.task_item_distance);
                    holder.task_item_type = (ImageView) convertView.findViewById(R.id.task_item_type);
                    holder.task_item_pay = (ImageView) convertView.findViewById(R.id.task_item_pay);

                    holder.task_item_distance.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_map_marker));
                    holder.task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                    holder.task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));

                    // address
                    holder.task_source_addr = (TextView ) convertView.findViewById(R.id.task_large_source_address);
                    holder.task_source_addr.setText( tasks.get(position).getDeliveryAddress()  );

                    holder.task_target_addr = (TextView) convertView.findViewById(R.id.task_large_target_address);
                    holder.task_source_addr.setText( tasks.get(position).getReceivingAddress()  );

                    // rating bar
                    holder.task_rating_bar = (RatingBar) convertView.findViewById(R.id.task_rating);
                    holder.task_rating_bar.setRating( (float) tasks.get(position).getRate() );

                    // type
                    holder.task_large_taskType = (TextView) convertView.findViewById(R.id.task_large_type);
                    holder.task_large_taskType.setText( tasks.get(position).getCategory() );

                    //payment
                    holder.task_large_payment = (TextView) convertView.findViewById(R.id.task_large_pay);
                    holder.task_large_payment.setText( tasks.get(position).getPayment() + "");
//                    Log.e("TaskAdapter","pos="+position+",source_address="+tasks.get(position).getDeliveryAddress() );

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
            case R.layout.item_task_small:
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(context).inflate(resId, null);
                    holder = new ViewHolder();
                    holder.task_item_type = (ImageView) convertView.findViewById(R.id.task_item_type);
                    holder.task_item_pay = (ImageView) convertView.findViewById(R.id.task_item_pay);

                    holder.task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                    holder.task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));

                    holder.task_source_addr = (TextView ) convertView.findViewById(R.id.task_small_source_address);
                    holder.task_source_addr.setText( tasks.get(position).getDeliveryAddress()  );

                    holder.task_target_addr = (TextView) convertView.findViewById(R.id.task_small_target_address);
                    holder.task_source_addr.setText( tasks.get(position).getReceivingAddress()  );

                    // type
                    holder.task_large_taskType = (TextView) convertView.findViewById(R.id.task_small_type);
                    holder.task_large_taskType.setText( tasks.get(position).getCategory() );

                    //payment
                    holder.task_large_payment = (TextView) convertView.findViewById(R.id.task_small_pay);
                    holder.task_large_payment.setText( tasks.get(position).getPayment() + "");

                    holder.task_small_status = (TextView) convertView.findViewById(R.id.task_status);
                    String status;
                    Task.TaskStatus code = tasks.get(position).getStatus();
                    switch (code)
                    {
                        case PUBLISHED:status="已发布，待接受";break;
                        case ACCEPTED:status="被接受";break;
                        case PROGRESS:status="进行中";break;
                        case ABORT:status="已放弃";break;
                        case PAUSED:status="已暂停";break;
                        case DELIVERED:status="已送达";break;
                        case COMPLETED:status="待评价";break;
                        case RATED:status="已结束";break;
                        default:status="状态不明";break;
                    }
                    holder.task_small_status.setText(status);

//                    Log.e("TaskAdapter","small::pos="+position+",source_address="+tasks.get(position).getDeliveryAddress() );

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
        }

        return convertView;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void removeTask(int index) {
        tasks.remove(index);
    }


    static class ViewHolder {
        ImageView task_item_distance, task_item_type, task_item_pay;
        TextView task_source_addr,task_target_addr,task_large_taskType,task_large_payment,task_small_status;
        RatingBar task_rating_bar;

    }
}
