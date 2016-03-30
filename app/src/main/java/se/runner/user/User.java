package se.runner.user;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import se.runner.request.HttpCallback;
import se.runner.request.HttpPost;

public class User
{
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

    public User(String account, String passwd)
    {
        this.account = account;
        this.passwd = passwd;
        this.nickname = "null";
        this.icon = "default.jpg";
        this.balance = 0;
        this.address = "null";
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

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAveragerate(float averagerate){
        this.averagerate = averagerate;
    }

    public boolean login()
    {
        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd",passwd);
        new HttpPost("/login", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null){
                    Log.e("User-login:","get is null");
                    return;
                }

                Log.e("User-Login:",get);

                if(get.equals("login success"))
                {
                    login = 1;
                    register = true;
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
                    Log.e("Fatal-Error","看不懂服务器发回来什么鬼东西");
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
                    Log.e("User-Logout:","get is null");
                else if(get.equals("logout success"))
                {
                    login = 0;
                }
                else
                {
                    Log.e("User-logout:","logout can't fail");
                    //// TODO: 3/30/16 User-logout failed
                }
            }
        }).execute();

        return login == 0;
    }

    public boolean register()
    {
        Log.e("User","Registering a new accout");

        ContentValues para = new ContentValues();

        para.put("account",account);
        para.put("passwd",passwd);
        new HttpPost("/register", para, new HttpCallback()
        {
            @Override
            public void onPost(String get)
            {
                if(get == null )  // reigster failed
                {
                    //// TODO: 3/30/16 USER register failed
                    Log.e("User-register","get string is null");
                }
                else
                {
                    Log.e("User-register","??"+get);
//                    try {
//                        JSONObject jsonObject = new JSONObject(get);
//
//                        nickname = (String)jsonObject.get("nickname");
//                        icon = (String) jsonObject.get("icon");
//                        balance = (int) jsonObject.get("balance");
//                        address = (String) jsonObject.get("address");
//                        login = 0;
//                        register = true;
//                    }
//                    catch ( JSONException ex )
//                    {
//                        ex.printStackTrace();
//                    }


                }
            }
        }).execute();

        return register;
    }

    public void refressh()
    {

    }
}
