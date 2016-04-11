package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;
import se.runner.user.User;

public class RegisterActivity extends AppCompatActivity {
    final public static int REGISTER_SET_IMAGE = 0x000A;
    final private String TAG="RegisterActivity";

    private User user;
    private Context context;

    @Bind(R.id.tool_bar)
    Toolbar tool_bar;

    @Bind(R.id.register_icon)
    ImageView register_icon;

    @Bind(R.id.register_user_name)
    MaterialEditText register_user_name;

    @Bind(R.id.register_nickname)
    MaterialEditText register_nickname;

    @Bind(R.id.register_passwd)
    MaterialEditText register_passwd;

    @Bind(R.id.register_passwd_reconfirm)
    MaterialEditText register_passwd_reconfirm;

    @Bind(R.id.register_address)
    MaterialEditText register_address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        register_icon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_user));

        context = RegisterActivity.this;

    }

    @OnClick(R.id.register_btn_confifrm)
    void register() {
        // check user name validation
        if( register_user_name.getText().toString() == null || register_user_name.getText().toString().equals(""))
        {
            new AlertDialog.Builder(context)
                    .setTitle("用户名不能为空")
                    .setMessage("请填上您的大名")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return ;
        }

        // passwd is not same
        if( register_passwd.getText().toString().equals(register_passwd_reconfirm.getText().toString()) == false )
        {
            new AlertDialog.Builder(context)
                    .setTitle("两次密码输入不符")
                    .setMessage("请重新确认密码")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return ;
        }

        user = new User(this,register_user_name.getText().toString(),register_passwd.getText().toString());
        user.setNickname(register_nickname.getText().toString());
        user.setAddress(register_address.getText().toString());

        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )  // reigster failed
                {
                    Log.e(TAG,"get string is null");

                    new AlertDialog.Builder(context)
                            .setTitle("连接错误")
                            .setMessage("请重试")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return;
                }
                else if(get.equals("already exist") )
                {
                    Log.e(TAG,"register failed for already exist");

                    new AlertDialog.Builder(context)
                            .setTitle("账户已存在")
                            .setMessage("请去登录")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
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
                    Log.e(TAG,"register response="+get);

                    new AlertDialog.Builder(context)
                            .setTitle("注册成功")
                            .setMessage("快去登录吧~")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
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
            }
        };

        user.register(httpCallback);
    }

    @OnClick(R.id.register_btn_cancel)
    void cancel() {
        finish();
    }

    @OnClick(R.id.register_icon)
    void setIcon() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.register_set_image)), REGISTER_SET_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REGISTER_SET_IMAGE:
                Uri uri = data.getData();
                register_icon.setImageURI(uri);
                break;
        }
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
