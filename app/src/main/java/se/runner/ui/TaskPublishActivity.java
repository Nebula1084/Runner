package se.runner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.widget.RunnerTimePicker;

public class TaskPublishActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.publish_location_pick_picker)
    ImageView publish_location_pick_picker;

    @Bind(R.id.publish_location_delivery_picker)
    ImageView publish_location_delivery_picker;

    @Bind(R.id.publish_emergency_picker)
    RatingBar publish_emergency_picker;

    private RunnerTimePicker publishPickTimePicker;
    private RunnerTimePicker publishDeliveryTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(new IconDrawable(this, FontAwesomeIcons.fa_arrow_left).actionBarSize());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        publish_location_pick_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));
        publish_location_delivery_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));

        publishPickTimePicker = new RunnerTimePicker(TaskPublishActivity.this);
        publishDeliveryTimePicker = new RunnerTimePicker(TaskPublishActivity.this);
    }

    @OnClick(R.id.publish_btn_cancel)
    void cancel() {
        finish();
    }

    @OnClick(R.id.publish_btn_confifrm)
    void confirm() {

    }

    @OnClick(R.id.publish_category_picker)
    void pickCategory() {

    }

    @OnClick(R.id.publish_location_pick_picker)
    void pickPickLocation() {

    }

    @OnClick(R.id.publish_location_delivery_picker)
    void pickDeliveryLocation() {

    }

    @OnClick(R.id.publish_time_pick_picker)
    void pickPickTime() {
        publishPickTimePicker.show();
    }

    @OnClick(R.id.publish_time_delivery_picker)
    void pickDeliveryTime() {
        publishDeliveryTimePicker.show();
    }

    @OnClick(R.id.publish_pay_picker)
    void pickPay() {

    }

    @OnClick(R.id.publish_consignee_picker)
    void pickConsignee() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
