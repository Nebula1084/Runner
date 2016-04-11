package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.task.Task;

public class TaskAcceptActivity extends AppCompatActivity {
    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.accept_location_pick)
    TextView accept_location_pick;

    @Bind(R.id.accept_time_pick)
    TextView accept_time_pick;

    @Bind(R.id.accept_location_delivery)
    TextView accept_location_delivery;

    @Bind(R.id.accept_time_delivery)
    TextView accept_time_delivery;

    @Bind(R.id.accept_category)
    TextView accetp_category;

    @Bind(R.id.accept_emergency)
    RatingBar accept_emergency;

    @Bind(R.id.accept_pay)
    TextView accept_pay;

    @Bind(R.id.accept_consignee)
    TextView accept_consignee;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra(Task.class.getName());

        accept_location_pick.setText(task.getDeliveryAddress());
        accept_location_delivery.setText(task.getReceivingAddress());
        accept_consignee.setText(task.getTaskConsignee());
    }

    @OnClick(R.id.accept_btn_confifrm)
    void confirm() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}