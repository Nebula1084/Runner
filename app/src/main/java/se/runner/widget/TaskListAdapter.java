package se.runner.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import se.runner.R;
import se.runner.task.Task;

public class TaskListAdapter extends BaseAdapter {
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
        switch (resId) {
            case R.layout.item_task_large:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(resId, null);
                    holder = new ViewHolder();
                    holder.task_item_distance = (ImageView) convertView.findViewById(R.id.task_item_distance);
                    holder.task_item_type = (ImageView) convertView.findViewById(R.id.task_item_type);
                    holder.task_item_pay = (ImageView) convertView.findViewById(R.id.task_item_pay);

                    holder.task_item_distance.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_map_marker));
                    holder.task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                    holder.task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
            case R.layout.item_task_small:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(resId, null);
                    holder = new ViewHolder();
                    holder.task_item_type = (ImageView) convertView.findViewById(R.id.task_item_type);
                    holder.task_item_pay = (ImageView) convertView.findViewById(R.id.task_item_pay);

                    holder.task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                    holder.task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));
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
    }
}
