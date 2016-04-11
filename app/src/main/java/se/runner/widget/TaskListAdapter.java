package se.runner.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import se.runner.R;

public class TaskListAdapter extends BaseAdapter {
    private Context context;
    private int count = 0;
    private int resId;

    public TaskListAdapter(Context context,int resId) {
        this.context = context;
        this.resId=resId;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        ImageView task_item_distance, task_item_type, task_item_pay;
        switch (resId){
            case R.layout.item_task_large:

                task_item_distance = (ImageView) view.findViewById(R.id.task_item_distance);
                task_item_type = (ImageView) view.findViewById(R.id.task_item_type);
                task_item_pay = (ImageView) view.findViewById(R.id.task_item_pay);

                task_item_distance.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_map_marker));
                task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));
                break;
            case R.layout.item_task_small:
                task_item_type = (ImageView) view.findViewById(R.id.task_item_type);
                task_item_pay = (ImageView) view.findViewById(R.id.task_item_pay);

                task_item_type.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_flag));
                task_item_pay.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_dollar));
                break;
        }

        return view;
    }
}
