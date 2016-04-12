package se.runner.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.runner.R;

/**
 * Created by zieng on 4/12/16.
 */

public class MyDialogFragment extends DialogFragment
{
//    private static Context context;
//
//    public static MyDialogFragment newInstance(Context ctx)
//    {
//        MyDialogFragment frag = new MyDialogFragment();
//        context = ctx;
//        return frag;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        int title = getArguments().getInt("title");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("请输入新的昵称");
//
//        // Set up the input
//        final EditText input = new EditText(context);
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT );
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                user.setNickname( input.getText().toString()  );
//
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }
}