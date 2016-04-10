package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.widget.CaptureActivityAnyOrientation;

public class ScanActivity extends AppCompatActivity {
    final public static String SCAN_URL = "scan_url";

    private String qrResult;

    private Intent result;
    @Bind(R.id.tool_bar)
    Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        result = new Intent();

        scan();

    }

    public void scan()
    {
        IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);  // beep sound effect
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(scanResult != null)
        {
            String re = scanResult.getContents();
            if(re != null)
            {
                Log.e("scan result = ", re);
                qrResult = re;
                result.putExtra(SCAN_URL,qrResult);
                confirm();
            }
        }
        else
        {
            Log.e("Scan Error","Nothing Scanned!");
        }
    }

    @OnClick(R.id.scan_confirm)
    void confirm(){
        setResult(RESULT_OK, result);
        finish();
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
