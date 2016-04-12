package se.runner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import se.runner.R;
import se.runner.task.Task;

public class TaskDetailFragment extends Fragment {
    final private String TAG = "TaskDetail";

    Task task;


    private TextView detail_runner;
    private TextView detail_source_address;
    private TextView detail_gain_time;
    private TextView detail_target_address;
    private TextView detail_delivery_time;
    private TextView detail_category;
    private TextView detail_payment;
    private TextView detail_consignee;
    private RatingBar detail_emergency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        task = (Task) getArguments().getSerializable(Task.class.getName());

        detail_runner = (TextView) view.findViewById(R.id.task_detail_runner);
        detail_source_address = (TextView) view.findViewById(R.id.task_detail_source_address);
        detail_gain_time = (TextView) view.findViewById(R.id.task_detail_gain_time);
        detail_target_address = (TextView) view.findViewById(R.id.task_detail_target_address);
        detail_delivery_time = (TextView) view.findViewById(R.id.task_detail_delivery_time);
        detail_category = (TextView) view.findViewById(R.id.task_detail_category);
        detail_payment = (TextView) view.findViewById(R.id.task_detail_pay);
        detail_consignee = (TextView) view.findViewById(R.id.task_detail_consignee);
        detail_emergency = ( RatingBar) view.findViewById(R.id.task_detail_emergency);

        if( task != null )
        {
            detail_runner.setText(task.getTaskShipper());
            detail_source_address.setText(task.getReceivingAddress());
            detail_gain_time.setText(task.getRequired_gain_time()+"");
            detail_target_address.setText(task.getDeliveryAddress());
            detail_delivery_time.setText( task.getRequired_delivery_time() +"");
            detail_category.setText( task.getCategory() );
            detail_payment.setText( task.getPayment() +"");
            detail_consignee.setText( task.getTaskConsignee() );
            detail_emergency.setRating( task.getEmergency() );
        }
        else
        {
            Log.e(TAG,"get a null task");
        }

        return view;
    }
}
