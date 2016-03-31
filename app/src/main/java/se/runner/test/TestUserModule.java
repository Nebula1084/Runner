package se.runner.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.user.User;

public class TestUserModule extends AppCompatActivity
{
    final private String TAG = "UserTest";
    private User user;
    private EditText editAccount;
    private EditText editPasswd;
    private EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_user_module);

        Log.e(TAG,"onCreate test module");

        editAccount = (EditText) findViewById(R.id.editAccount);
        editPasswd = (EditText) findViewById(R.id.editPasswd);
        editAddress = (EditText) findViewById(R.id.editAdd);
    }

    @OnClick(R.id.regButton)
    public void register()
    {
        String account = editAccount.getText().toString();
        String passwd = editPasswd.getText().toString();

        user = new User(this,account,passwd);
        user.register();
    }

    @OnClick(R.id.loginButton)
    public void login()
    {
        if( user == null )
            Log.e(TAG,"register before login");
        else
            user.login();
    }
}
