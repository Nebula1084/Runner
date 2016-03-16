package se.runner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.test.Greeting;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.greet)
    Button greet;

    @OnClick(R.id.greet)
    void greeting() {
        new Greeting(MainActivity.this).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


}
