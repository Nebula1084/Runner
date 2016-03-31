package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.test.TestActivity;
import se.runner.test.TestUserModule;
import se.runner.user.User;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    User user;

    @OnClick(R.id.login_btn_login)
    void login() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @OnClick(R.id.login_btn_test)
    void test() {
        startActivity(new Intent(LoginActivity.this, TestActivity.class));

//        startActivity(new Intent( LoginActivity.this , TestUserModule.class));
//        user = new User(this,"android","android");
//        user.register();
//
////        user.login();
//
//        Log.e(TAG,user.getAddress());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("methodName").equals("HttpDone")){
            checkValue();
        }
    }

    public void checkValue()
    {
        Log.e(TAG,user.getAddress());
    }
}
