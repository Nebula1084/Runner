package se.runner.test;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;
import se.runner.request.HttpCallback;
import se.runner.request.HttpGet;
import se.runner.ui.LoginActivity;
import se.runner.user.User;

public class TestActivity extends AppCompatActivity {

    private User user;
    final private String TAG="TestActivity";

    @OnClick(R.id.test_btn_greet)
    void greeting() {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", "test_user");
//        new HttpGet("/greeting", contentValues, new HttpCallback() {
//            @Override
//            public void onPost(String get) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
//                builder.setMessage(get);
//                builder.create().show();
//            }
//        }).execute();
//        startActivity(new Intent( TestActivity.this , TestUserModule.class));
        user.register();
    }

    @OnClick(R.id.loginBtn)
    void asdfjaddfjsk()
    {
        user.login();
    }

    @OnClick(R.id.addressBtn)
    void azdaffdf()
    {
        user.setAddress("earth");
    }

    @OnClick(R.id.nickBtn)
    void nickkkkk()
    {
        user.setNickname("my name is nick");
    }

    @OnClick(R.id.payBtn)
    void payyyyy()
    {
        User other = new User(this,"test","test");
        user.pay(other, 3);
    }

    @OnClick(R.id.iconBtn)
    void setIconnnnn()
    {
        user.setIcon("icon.png");
    }

    @OnClick(R.id.rateBtn)
    void rateeeeed()
    {
        float r = 24.5f;
        user.setAveragerate(r);
    }

    @OnClick(R.id.depositBtn)
    void deposssss()
    {
        user.deposit(234.0);
    }

    @OnClick(R.id.infoBtn)
    void infffffo()
    {
        user.getInfo();
    }

    @OnClick(R.id.passwdBtn)
    void setpasssssswdd()
    {
        user.setPasswd("nice weather");
    }

    @OnClick(R.id.locateBtn)
    void locateeeee()
    {
        user.setLongtitude(34.45);

        user.setLatitude(5.03);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        user = new User(this,"hello","android");

    }
}
