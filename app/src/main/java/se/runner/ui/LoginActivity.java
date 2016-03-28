package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.test.TestActivity;

public class LoginActivity extends AppCompatActivity {


    @OnClick(R.id.login_btn_login)
    void login() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @OnClick(R.id.login_btn_test)
    void test() {
        startActivity(new Intent(LoginActivity.this, TestActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
