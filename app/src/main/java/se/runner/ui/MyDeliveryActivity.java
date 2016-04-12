package se.runner.ui;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.task.Task;

public class MyDeliveryActivity extends AppCompatActivity {
    final private String TAG = "DeliveryActivity";

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.mydelivery_location_pick_picker)
    ImageView mydelivery_location_pick_picker;

    @Bind(R.id.mydelivery_location_delivery_picker)
    ImageView mydelivery_location_delivery_picker;

    @Bind(R.id.mydelivery_task_id)
    ImageView mydelivery_task_id;

    @Bind(R.id.mydelivery_pick_check)
    CheckBox mydelivery_pick_check;

    @Bind(R.id.mydelivery_delivery_check)
    CheckBox mydelivery_delivery_check;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydelivery);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if( intent != null)
        {
            task = (Task) intent.getSerializableExtra(Task.class.getName());
        }
        else
        {
            task = new Task();
            Log.e(TAG," intent is null ");
        }

        mydelivery_location_pick_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));
        mydelivery_location_delivery_picker.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(32));
        mydelivery_task_id.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_qrcode).sizeDp(32));



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

    @OnClick(R.id.mydelivery_task_id)
    void qrCode()
    {
        Intent intent = new Intent(MyDeliveryActivity.this, QRCodeActivity.class);
        intent.putExtra("tid",task.getId()+"");
        startActivity(intent);
    }
}