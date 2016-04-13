package se.runner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.gc.materialdesign.views.CheckBox;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.test.TestActivity;
import se.runner.user.User;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    User user;
    private MaterialEditText account_editText;
    private MaterialEditText passwd_editText;
    private Context context;

    private boolean save_passwd = false;
    private boolean auto_login = false;

    @Bind(R.id.checkBox_passwd_record)
    CheckBox record_passwd_checkbox;

    @Bind(R.id.checkBox_auto_login)
    CheckBox auto_login_checkbox;

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.login_icon)
    ImageView login_icon;


    @OnClick(R.id.login_btn_login)
    void login()
    {
        HttpCallback httpCallback = new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null){
                    Log.e(TAG,"get is null");
                    return;
                }

                Log.e(TAG,"login response="+get);

                if(get.equals("account not exist."))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("用户不存在")
                            .setMessage("快来注册一个吧")
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
                else if(get.equals("passwd incorrect"))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("密码错误")
                            .setMessage("请检查密码拼写")
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
                    confirmLogin();
                }
            }
        };

        Log.e(TAG,"prepare login as:"+account_editText.getText().toString()+passwd_editText.getText().toString());

        user = new User(this,account_editText.getText().toString(),passwd_editText.getText().toString());
        user.login(httpCallback);


    }

    public void confirmLogin()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        Log.e(TAG,"Going to serialize user");
        intent.putExtra( "account",  user.getAccount() );
        intent.putExtra("passwd", user.getPasswd());

        startActivity(intent);

        // quit login activity so main activity won't back to it in a normal way
        finish();
    }

    @OnClick(R.id.login_btn_test)
    void test() {
        startActivity(new Intent(LoginActivity.this, TestActivity.class));
    }

    @OnClick(R.id.login_register)
    void register()
    {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Iconify
                .with(new FontAwesomeModule())
                .with(new MaterialCommunityModule());
        login_icon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_user));

        context = LoginActivity.this; // save context for using dialog

        // get edit text
        account_editText = (MaterialEditText) findViewById(R.id.login_account_text);
        passwd_editText = (MaterialEditText) findViewById(R.id.login_passwd_text);

        auto_login_checkbox.setOncheckListener(new CheckBox.OnCheckListener()
        {
            @Override
            public void onCheck(CheckBox view, boolean check)
            {
                Log.e(TAG,"check = "+check);
                record_passwd_checkbox.setChecked( auto_login_checkbox.isCheck() );
            }
        });

        Intent intent = getIntent();
        if( intent != null && intent.getBooleanExtra("logout",false) )
        {
            save_auto_login_state( false );
            save_record_passwd_state( false );
            save_account_content("null");
            save_passwd_content("null");
            save_passwd = false;
            auto_login = false;
        }
        else
        {
            save_passwd = load_record_passwd_state();
            auto_login = load_auto_login_state();

            auto_login_checkbox.setChecked( auto_login );

            if( save_passwd )
            {
                String stored_passwd = load_passwd_content();
                String stored_account = load_account_content();

                if( stored_passwd.equals("null") == false && stored_account.equals("null") == false )  // means the passwd is stored before
                {
                    passwd_editText.setText( stored_passwd );
                    account_editText.setText( stored_account );
                    record_passwd_checkbox.setChecked( save_passwd );

                    if( auto_login )
                    {
                        login();
                    }
                }
            }
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();

        record_passwd_checkbox.setChecked( load_record_passwd_state() );
        auto_login_checkbox.setChecked( load_auto_login_state() );
    }

    @Override
    public void onPause()
    {
        super.onPause();

        save_auto_login_state( auto_login_checkbox.isCheck() );
        save_record_passwd_state( record_passwd_checkbox.isCheck() );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        save_auto_login_state( auto_login_checkbox.isCheck() );
        save_record_passwd_state( record_passwd_checkbox.isCheck() );

        if( record_passwd_checkbox.isCheck() )
        {
            save_account_content( account_editText.getText().toString() );
            save_passwd_content( passwd_editText.getText().toString() );
        }
    }

    private boolean load_record_passwd_state()
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("save_passwd", false);
    }

    private void save_record_passwd_state(final boolean isChecked)
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("save_passwd", isChecked);

        editor.commit();
    }

    private boolean load_auto_login_state()
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("auto_login", false);
    }

    private void save_auto_login_state(final boolean isChecked)
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("auto_login", isChecked);

        editor.commit();
    }

    private void save_passwd_content(final String passwd)
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("passwd",passwd);

        editor.commit();
    }

    private void save_account_content(final String account)
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account",account);

        editor.commit();
    }

    private String load_passwd_content()
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString("passwd", "null");
    }

    private String load_account_content()
    {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString("account", "null");
    }

}