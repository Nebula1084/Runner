package se.runner.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.runner.R;

public class ContactActivity extends AppCompatActivity
{
    final private static String TAG = "ContactActivity";

    final public static String LIST_DATA = "contactList";

    private Intent result;
    private List<String> contactList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;


    @Bind(R.id.tool_bar)
    Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        setSupportActionBar(tool_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        result = new Intent();

        listView = (ListView) findViewById(R.id.contact_list_view);

        Intent intent = getIntent();
        if( intent != null )
        {
            String [] userList = intent.getStringArrayExtra("contactList");
            if( userList != null && userList.length != 0 )
            {
                contactList = new ArrayList<String>( Arrays.asList( userList ));
            }
            else
            {
                contactList = new ArrayList<>();
                contactList.add("<Empty-List>");
            }

            arrayAdapter = new ArrayAdapter<String>(this,R.layout.contact_list_adapter,R.id.contact_user_name,contactList);
            listView.setAdapter(arrayAdapter);

        }
        else
        {
            Log.e(TAG," intent is null ");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            Log.e(TAG,"android.R.id.home");

            String [] strings = new String[ contactList.size() ];
            strings = contactList.toArray(strings);

            result.putExtra(LIST_DATA, strings );
            setResult(RESULT_OK, result);

            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.contact_add)
    void add()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入账户名");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("确定添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String tmpUser = input.getText().toString();
                if( contactList.size() == 1 && contactList.get(0).equals("<Empty-List>") )
                    contactList.clear();
                contactList.add(tmpUser);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
