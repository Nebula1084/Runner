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
    private double balance;
    private String address;
    private double averagerate;
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

    public double getBalance() {
        return balance;
    }

    public double getAveragerate() {
        return averagerate;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
        if( !isLogin() )
            return;

        ContentValues para = new ContentValues();
        para.put("passwd", passwd);
        //// TODO: 3/31/16 update passwd to server
    }

    public void setNickname(final String nickname)
    {
        this.nickname = nickname;
        if( !isLogin() )
            return;

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("nickname",nickname);
        new HttpPost("/setnickname", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"setnickname return null");
                else if( get.equals("not exit"))
                    Log.e(TAG,"set nickname::user not exist");
                else
                {
                    //// TODO: 3/31/16 update nickname
                    parseServerResponse(get);
                    new AlertDialog.Builder(context)
                            .setTitle("设置昵称成功")
                            .setMessage("您的昵称更改为："+nickname)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
        if( !isLogin() )
            return;
        //todo update file/image
    }

    public void setAddress(final String address)
    {
        this.address = address;
        if( !isLogin() )
            return;

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
                else if( get.equals("not exit"))
                {
                    Log.e(TAG, "user not exist");
                    new AlertDialog.Builder(context)
                            .setTitle("账户不存在")
                            .setMessage("请重试")
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
                else
                {
                    //// TODO: 3/31/16 set address
                    parseServerResponse(get);

                    new AlertDialog.Builder(context)
                            .setTitle("设置地址成功")
                            .setMessage("您的地址更改为："+address)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();
    }

    public void setAveragerate(final float averagerate)
    {
        this.averagerate = averagerate;
        if( !isLogin() )
            return;

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("averagerate",averagerate+"");
        new HttpPost("/setaveragerate", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set averagerate return null");
                else if( get.equals("not exit"))
                {
                    Log.e(TAG, "user not exist");
                    new AlertDialog.Builder(context)
                            .setTitle("账户不存在")
                            .setMessage("请重试")
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
                else
                {
                    //// TODO: 3/31/16 set averagerate
                    parseServerResponse(get);
                    new AlertDialog.Builder(context)
                            .setTitle("更改评分成功")
                            .setMessage("您的评分为："+averagerate)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();
    }

    public void deposit(double amount)
    {
        if(!isLogin()) {
            Log.e(TAG,"deposit should after login");
            return;
        }

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("amount",""+amount);
        new HttpPost("/deposit", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"deposit return null");
                else if( get.equals("not exit"))
                {
                    Log.e(TAG, "set nickname::user not exist");
                    new AlertDialog.Builder(context)
                            .setTitle("账户不存在")
                            .setMessage("请重试")
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
                else
                {
//                    parseServerResponse(get);
                    getInfo();

                    new AlertDialog.Builder(context)
                            .setTitle("存钱成功")
                            .setMessage("您的余额为："+balance)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();
    }

    public void pay(final User other, final double amount)
    {
        if( !isLogin() )
            return ;

        ContentValues para = new ContentValues();
        para.put("account1",account);
        para.put("account2",other.getAccount());
        para.put("amount",amount+"");
        new HttpPost("/pay", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                {
                    Log.e(TAG, "Pay return null");

                    new AlertDialog.Builder(context)
                            .setTitle("连接错误")
                            .setMessage("请重试")
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
                else
                {
                    parseServerResponse(get);
                    new AlertDialog.Builder(context)
                            .setTitle("转账成功")
                            .setMessage("您成功为"+(String)other.getAccount()+"转账"+amount+"。您的余额为："+balance)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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

                if(get.equals("account not exist."))
                {
                    //// TODO: 3/30/16 login-failed:no such accout
                    new AlertDialog.Builder(context)
                            .setTitle("用户不存在")
                            .setMessage("快来注册一个吧")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
                else if(get.equals("passwd incorrect"))
                {
                    //// TODO: 3/30/16 login-faild: incorrect passwd
                    new AlertDialog.Builder(context)
                            .setTitle("密码错误")
                            .setMessage("请检查密码拼写")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
                else
                {
                    login = 1;
                    // get account info from server
                    //parseServerResponse(get);
                    getInfo();

                    new AlertDialog.Builder(context)
                            .setTitle("登录成功")
                            .setMessage("快去使用吧~")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();

        return login == 1;
    }

    public boolean logout()
    {
        if( !isLogin() )
            return false;

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

                if( login == 0 )
                {
                    new AlertDialog.Builder(context)
                            .setTitle("登出成功")
                            .setMessage("时刻等着你哦~")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
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
        }).execute();

        return login == 0;
    }

    public boolean register()
    {
        if( isLogin() )
        {
            Log.e(TAG,"you already login, can't register");
            return false;
        }

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

                    new AlertDialog.Builder(context)
                            .setTitle("连接错误")
                            .setMessage("请重试")
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

                    return;
                }
                else if(get.equals("already exit") )
                {
                    Log.e(TAG,"register failed for already exit");
                    register = true;

                    new AlertDialog.Builder(context)
                            .setTitle("账户已存在")
                            .setMessage("请去登录")
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
                else
                {
                    Log.e(TAG,"register response="+get);
                    parseServerResponse(get);

                    register = true;

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
        }).execute();


        return register;
    }

    public void getInfo()
    {
        if( !isLogin() )
            return;

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

            account = (String) jsonObject.get("account");
            passwd = (String ) jsonObject.get("passwd");
            nickname = (String)jsonObject.get("nickname");
            icon = (String) jsonObject.get("icon");
            balance = (double) jsonObject.get("balance");
            address = (String) jsonObject.get("address");
            averagerate = (double) jsonObject.get("averagerate");
//            timestamp = (int) jsonObject.get("timestamp");
//            Log.e(TAG,"register result:address="+getAddress());
        }
        catch ( JSONException ex )
        {
            ex.printStackTrace();
        }
    }
}
