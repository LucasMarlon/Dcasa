package projeto.emp.dcasa.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.HashMap;

import projeto.emp.dcasa.models.User;
import projeto.emp.dcasa.views.LoginActivity;
import projeto.emp.dcasa.views.MainActivity;

public class MySharedPreferences {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "Pref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME_USER = "name_user";
    public static final String KEY_lAST_NAME_USER = "last_name_user";
    public static final String KEY_PHONE_USER = "phone_user";
    public static final String KEY_LOGIN_USER = "login";
    public static final String KEY_PASSWORD_USER = "password_user";

    public MySharedPreferences(Context context){
        this.mContext = context;
        mPref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public void saveUser(User user){
        mEditor.putBoolean(IS_USER_LOGIN, true);
        mEditor.putString(KEY_NAME_USER, user.getName());
        mEditor.putString(KEY_lAST_NAME_USER, user.getLastName());
        mEditor.putString(KEY_PHONE_USER, user.getPhone());
        mEditor.putString(KEY_LOGIN_USER, user.getLogin());
        mEditor.putString(KEY_PASSWORD_USER, user.getPassword());
        mEditor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<String, String>();
        userDetails.put(KEY_NAME_USER, mPref.getString(KEY_NAME_USER, null));
        userDetails.put(KEY_lAST_NAME_USER, mPref.getString(KEY_lAST_NAME_USER, null));
        userDetails.put(KEY_PHONE_USER, mPref.getString(KEY_PHONE_USER, null));
        userDetails.put(KEY_LOGIN_USER, mPref.getString(KEY_LOGIN_USER, null));
        userDetails.put(KEY_PASSWORD_USER, mPref.getString(KEY_PASSWORD_USER, null));

        return userDetails;
    }

    public boolean checkLogin(){
        if(this.isUserLoggedIn()){
            Intent i = new Intent(mContext, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);

            return true;
        }
        return false;
    }

    public boolean isUserLoggedIn(){
        return mPref.getBoolean(IS_USER_LOGIN, false);
    }


    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}