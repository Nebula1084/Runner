package se.runner.user;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;

public class User implements Serializable
{
    final private String TAG = "USER";
    final private static boolean DEBUG = true;

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
    private double latitude;
    private double longtitude;
    private int launchTaskNum;
    private int takeTaskNum;
    private List<String> contactList;
    transient Context context;

    public User(String account, String passwd)
    {
        this.account = account;
        this.passwd = passwd;
        this.nickname = "local_null";
        this.icon = "local_default.jpg";
        this.balance = 0;
        this.address = "local_null";
        this.login = 0;
        launchTaskNum = 0;
        takeTaskNum = 0;
        register = false;
        contactList = new ArrayList<>();
    }

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
        launchTaskNum = 0;
        takeTaskNum = 0;
        register = false;
        contactList = new ArrayList<>();
    }

    public boolean isRegistered(HttpCallback httpCallback)
    {
        //// TODO: 4/1/16 need a entrance for validate register info
        ContentValues para = new ContentValues();
        para.put("account",account);

        new HttpPost("/checkuser",para,httpCallback).execute();

        return register;
    }

    // for outer function call
    public boolean checkUser(HttpCallback httpCallback)
    {
        ContentValues para = new ContentValues();
        para.put("account", account);

        new HttpPost("/checkuser", para, httpCallback ).execute();

        return false;
    }

    // for inner use
    public boolean checkUser()
    {
        ContentValues para = new ContentValues();
        para.put("account", account);

        if( register == true )
            return register;

        new HttpPost("/checkuser", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"check account return null ");
                else if( get.equals("yes"))
                {
                    register = true;
                    new AlertDialog.Builder(context)
                            .setTitle("注册信息")
                            .setMessage("此账户已经注册")
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
                    register = false;
                    new AlertDialog.Builder(context)
                            .setTitle("注册信息")
                            .setMessage("此账户还未注册")
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

        return false;
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

    public int getLaunchTaskNum()
    {
        return launchTaskNum;
    }

    public int getTakeTaskNum()
    {
        return takeTaskNum;
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
        para.put("account",account);
        new HttpPost("/setpasswd", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set passwd return null ");
                else
                {
                    parseServerResponse(get);

                    new AlertDialog.Builder(context)
                            .setTitle("密码修改成功")
                            .setMessage("请务必牢记您的密码")
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
                else if( get.equals("not exist"))
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
                else if( get.equals("not exist"))
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
                else if( get.equals("not exist"))
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

    public void setLatitude(double l)
    {
        latitude = l;
        if( !isLogin() )
        {
            Log.e(TAG,"not login,won't update to server");
            return;
        }

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("latitude",latitude+"");
        new HttpPost("/setlatitude", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set latitude return null");
                else
                {
                    parseServerResponse(get);
                }
            }
        }).execute();
    }

    public void setLongtitude(double l)
    {
        longtitude = l;
        if( !isLogin() )
        {
            Log.e(TAG,"not login,won't update to server");
            return;
        }

        ContentValues para = new ContentValues();
        para.put("account",account);
        para.put("longtitude",longtitude+"");
        new HttpPost("/setlongtitude", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if( get == null )
                    Log.e(TAG,"set longtitude return null");
                else
                {
                    parseServerResponse(get);
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
                else if( get.equals("not exist"))
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

    public boolean login(HttpCallback httpCallback)
    {
        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd",passwd);

        new HttpPost("/login",para,httpCallback).execute();

        return true;
    }

    public boolean login()
    {
        HttpCallback httpCallback = new HttpCallback()
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
                    register = false;
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
                    register = true;
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
                    register = true;
                    // get account info from server
                    //parseServerResponse(get);
                    getInfo();

                    if(DEBUG)
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("DEBUG::登录成功")
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
            }
        };

        return login(httpCallback);
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

    public boolean register(HttpCallback httpCallback)
    {
        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd", passwd);
        para.put("nickname",nickname);
        para.put("address",address);

        new HttpPost("/register",para, httpCallback).execute();

        return true;
    }

    public boolean register()
    {
        if( isLogin() )
        {
            Log.e(TAG,"you already login, can't register");
            return false;
        }

        Log.e(TAG, "Registering a new accout");

        HttpCallback httpCallback = new HttpCallback()
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
                else if(get.equals("already exist") )
                {
                    Log.e(TAG,"register failed for already exist");
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
        };


        return register(httpCallback);
    }

    public void getInfo(HttpCallback httpCallback)
    {
        ContentValues para = new ContentValues();
        para.put("account",account);


        new HttpPost("/info",para,httpCallback).execute();

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

                    if(DEBUG)
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Debug")
                                .setMessage(get)
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
            averagerate = (double) jsonObject.get("avgrate");
            latitude = (double) jsonObject.get("latitude");
            longtitude = (double) jsonObject.get("longtitude");
            launchTaskNum = (int) jsonObject.get("launchTaskNum");
            takeTaskNum = (int ) jsonObject.get("takeTaskNum");
            contactList = (List<String>) jsonObject.get("contacts");

//            timestamp = (int) jsonObject.get("timestamp");
//            Log.e(TAG,"register result:address="+getAddress());
        }
        catch ( JSONException ex )
        {
            ex.printStackTrace();
        }
    }
}
