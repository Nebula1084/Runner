package se.runner.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.Text;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import se.runner.R;
import se.runner.user.User;

public class UserCenterFragment extends Fragment implements View.OnClickListener {
    private final String TAG= "UerCenter";

    final public static int CONTACT_LIST = 1 ;

    private ImageView user_icon, user_contacts_arrow, user_address_arrow, user_logout_arrow, user_edit, user_phone_call;
    private LinearLayout user_address, user_contacts, user_logout;

    private TextView nickname;
    private TextView phone;
    private TextView deliveryNum;
    private TextView publishNum;

    private Context context;

    private static User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        user_icon = (ImageView) view.findViewById(R.id.user_icon);
        user_icon.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_user));

        user_contacts_arrow = (ImageView) view.findViewById(R.id.user_contacts_arrow);
        user_contacts_arrow.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_angle_right));

        user_address_arrow = (ImageView) view.findViewById(R.id.user_address_arrow);
        user_address_arrow.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_angle_right));

        user_logout_arrow = (ImageView) view.findViewById(R.id.user_logout_arrow);
        user_logout_arrow.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_sign_out));

        user_edit=(ImageView)view.findViewById(R.id.user_edit);
        user_edit.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_edit));
        user_edit.setOnClickListener(this);

        user_phone_call=(ImageView)view.findViewById(R.id.user_phone_call);
        user_phone_call.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_phone));

        user_address = (LinearLayout) view.findViewById(R.id.user_address);
        user_address.setOnClickListener(this);

        user_contacts = (LinearLayout) view.findViewById(R.id.user_contacts);
        user_contacts.setOnClickListener(this);

        user_logout = (LinearLayout) view.findViewById(R.id.user_logout);
        user_logout.setOnClickListener(this);

        nickname = (TextView) view.findViewById(R.id.user_center_nickname);
        phone = (TextView) view.findViewById(R.id.user_center_phone);

        deliveryNum = (TextView) view.findViewById(R.id.user_delivery_num);
        publishNum = (TextView) view.findViewById(R.id.user_publish_num);

        Bundle bundle = getArguments();
        if( bundle != null)
        {
            user = (User) bundle.getSerializable(User.class.getName());
        }
        else
        {
            Log.e(TAG,"bundle is null");
            user = new User("null","null");
        }

        nickname.setText( user.getNickname() );
        phone.setText(user.getAccount());
        deliveryNum.setText( user.getTakeTaskNum()+"" );
        publishNum.setText( user.getLaunchTaskNum()+"" );

        context = getActivity();

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_address:
                startActivity(new Intent(getContext(), AdderssActivity.class));
                break;
            case R.id.user_contacts:
                Intent intent = new Intent(getContext(), ContactActivity.class);

                String [] strings = new String[ user.getContactList().size() ];
                strings = user.getContactList().toArray(strings);
                intent.putExtra("contactList", strings );
                Log.e(TAG,"into contact activity, request code = "+ CONTACT_LIST);
                startActivityForResult( intent, CONTACT_LIST );

                break;
            case R.id.user_logout:
                logout();
                break;
            case R.id.user_edit:
                // TODO: 4/12/16 user edit
//                final EditText input = new EditText(context);
                break;
        }
    }


    public void confirmChangeNickname()
    {

    }

    public void logout()
    {
        // TODO: 4/12/16 save local changes to server

        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.putExtra("logout",true);

        startActivity(intent);
        getActivity().finish();
    }

    public void upload_ok()
    {
        getActivity().finish();
    }
}
