package se.runner.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;

public class RegisterActivity extends AppCompatActivity {
    final public static int REGISTER_SET_IMAGE = 0x000A;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        register_icon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_user));
    }

    @OnClick(R.id.register_btn_confifrm)
    void register() {

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
