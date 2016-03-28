package se.runner.test;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.request.HttpGet;

public class TestActivity extends AppCompatActivity {

    @OnClick(R.id.test_btn_greet)
    void greeting() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "test_user");
        new HttpGet("/greeting", contentValues, new HttpCallback() {
            @Override
            public void onPost(String get) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                builder.setMessage(get);
                builder.create().show();
            }
        }).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }
}
