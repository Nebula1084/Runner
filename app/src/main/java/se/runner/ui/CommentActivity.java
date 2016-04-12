package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.task.Task;

public class CommentActivity extends AppCompatActivity {

    final String TAG = "CommetActivity";

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.comment_runner)
    TextView comment_runner;

    @Bind(R.id.comment_comment)
    MaterialEditText comment_comment;

    @Bind(R.id.comment_rating)
    RatingBar comment_rating;

    private Task task;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        context = this;
        Intent intent = getIntent();
        if( intent != null )
        {
            task = (Task) intent.getSerializableExtra(Task.class.getName());
        }
        else
        {
            Log.e(TAG,"intent is null");
            task = new Task();
        }

        comment_runner.setText( task.getTaskShipper() );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @OnClick(R.id.comment_btn)
    void comment()
    {
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                {
                    Log.e(TAG,"server response null");
                }
                else if( get.equals("success"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("评价任务成功")
                            .setMessage("评价让我们做的更好！")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
                                }
                            })
                            .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else
                {
                    new AlertDialog.Builder(context)
                            .setTitle("恭喜你，你发现了一个隐藏BUG")
                            .setMessage("快反馈给程序员")
                            .setPositiveButton("我发誓我会反馈", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
                                }
                            })
                            .setNegativeButton("我保证我会反馈", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        };

        String commetStr = comment_comment.getText().toString();
        double rate =  comment_rating.getRating();

        task.rate(task.getId(),rate,commetStr,httpCallback);
    }
}
