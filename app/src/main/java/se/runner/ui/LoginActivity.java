package se.runner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;

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

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.login_icon)
    ImageView login_icon;


    @OnClick(R.id.login_btn_login)
    void login() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @OnClick(R.id.login_btn_test)
    void test() {
        startActivity(new Intent(LoginActivity.this, TestActivity.class));
    }

    @OnClick(R.id.login_register)
    void register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Iconify
                .with(new FontAwesomeModule())
                .with(new MaterialCommunityModule());
        login_icon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_user));
        login();
    }

}