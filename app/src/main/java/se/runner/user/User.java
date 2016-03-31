package se.runner.user;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;
import se.runner.ui.LoginActivity;

public class User
{
    final private String TAG = "USER";


    private String account;
    private String passwd;
    private String nickname;
    private String icon;
    private int balance;
    private String address;
    private float averagerate;
    private int login;
    private int timestamp;
    private boolean register;
    Context context;

    public User(Context ctx, String account, String passwd)
    {
        context = ctx;
        this.account = account;
        this.passwd = passwd;
        this.nickname = "local_null";
        this.icon = "local_default.jpg";
        this.balance = 0;
        this.address = "local_null";
        this.login = 0;
        register = false;
    }

    public boolean isRegistered()
    {
        return register;
    }

    public boolean isLogin()
    {
        return login == 1;
    }

    public String getAccount() {
        return account;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    public String getIcon() {
        return icon;
    }

    public int getBalance() {
        return balance;
    }

    public float getAveragerate() {
        return averagerate;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;

        ContentValues para = new ContentValues();
        para.put("passwd", passwd);
        //// TODO: 3/31/16 update passwd to server
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("nickname",nickname);
        new HttpPost("/setnickname", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null)
                    Log.e(TAG,"setnickname return null");
                else
                {
                    //// TODO: 3/31/16 update nickname
                    parseServerResponse(get);
                }
            }
        }).execute();
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
        //todo update file/image
    }

    public void setAddress(String address)
    {
        this.address = address;

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("address",address);
        new HttpPost("/setaddress", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null)
                    Log.e(TAG,"set address return null");
                else
                {
                    //// TODO: 3/31/16 set address
                    parseServerResponse(get);
                }
            }
        }).execute();
    }

    public void setAveragerate(float averagerate)
    {
        this.averagerate = averagerate;

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("averagerate",averagerate);
        new HttpPost("/setaveragerate", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set averagerate return null");
                else
                {
                    //// TODO: 3/31/16 set averagerate
                    parseServerResponse(get);
                }
            }
        }).execute();
    }

    public boolean login()
    {
        if(!register)
            Log.e(TAG,"you haven't registered yet!");

        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd",passwd);
        new HttpPost("/login", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null){
                    Log.e(TAG,"get is null");
                    return;
                }

                Log.e(TAG,"login response="+get);

                if(get.equals("login success"))
                {
                    login = 1;
                    register = true;
                    // get account info from server
                    getInfo();
                }
                else if(get.equals("account not exist."))
                {
                    //// TODO: 3/30/16 login-failed:no such accout
                }
                else if(get.equals("passwd incorrect"))
                {
                    //// TODO: 3/30/16 login-faild: incorrect passwd
                }
                else
                {
                    Log.e(TAG,"看不懂服务器发回来什么鬼东西");
                }
            }
        }).execute();

        return login == 1;
    }

    public boolean logout()
    {
        ContentValues para = new ContentValues();

        para.put("accout", account);
        new HttpPost("/logout", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null)
                    Log.e(TAG,"get is null");
                else if(get.equals("logout success"))
                {
                    login = 0;
                }
                else
                {
                    Log.e(TAG,"logout can't fail");
                    //// TODO: 3/30/16 User-logout failed
                }
            }
        }).execute();

        return login == 0;
    }

    public boolean register()
    {
        Log.e(TAG, "Registering a new accout");

        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd", passwd);
        new HttpPost("/register", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )  // reigster failed
                {
                    //// TODO: 3/30/16 USER register failed
                    Log.e(TAG,"get string is null");
                }
                else
                {
                    Log.e(TAG,"register response="+get);
                    parseServerResponse(get);

                    if( register )
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("注册成功")
                                .setMessage("快去登录吧~")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }

            }
        }).execute();


        return register;
    }

    public void getInfo()
    {
        ContentValues para = new ContentValues();
        para.put("account",account);
        new HttpPost("/info", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"get info return null");
                else
                {
                    // update info from server
                    parseServerResponse(get);
                }
            }
        }).execute();
    }

    public void refresh()
    {
        int localTimeStamp = timestamp;

        ContentValues para = new ContentValues();
        para.put("account",account);
        new HttpPost("/timestamp", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if ( get == null )
                    Log.e(TAG,"get timestamp reurn null");
                else
                {
                    //// TODO: 3/31/16 get timestammp from server
                }
            }
        }).execute();

        if( timestamp < localTimeStamp )  // local is newest, need update to local
        {
            timestamp = localTimeStamp;
            //// TODO: 3/31/16 update local info to server
        }
    }

    public void parseServerResponse(String response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);

            nickname = (String)jsonObject.get("nickname");
            icon = (String) jsonObject.get("icon");
            balance = (int) jsonObject.get("balance");
            address = (String) jsonObject.get("address");
//            timestamp = (int) jsonObject.get("timestamp");
            login = 0;
            register = true;
//            Log.e(TAG,"register result:address="+getAddress());
        }
        catch ( JSONException ex )
        {
            ex.printStackTrace();
        }
    }
}
