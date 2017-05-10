package zetrixweb.com.bloodnow.extra;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;
import zetrixweb.com.bloodnow.pojo.SignupPojo;



public class CommonUtils {

    public static SharedPreferences.Editor editor;
    public static final String SF_BLOOD_NOW_APP = "SF_BLOOD_NOW_APP";
    public static final String SF_LOGIN_DATA2 = "SF_LOGIN_DATA2";
    public static final String SF_LOGIN_DATA1 = "SF_LOGIN_DATA1";
    public static SharedPreferences sharedPreferences;

    public static void setStringSharedPref(Context context, String key, String value)
    {
        setEditor(context);
        editor.putString(key,value);
        editor.commit();
    }

    public static void setSharedPrefs(Context context)
    {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(SF_BLOOD_NOW_APP,Context.MODE_PRIVATE);
    }

    public static void setEditor(Context context)
    {
        setSharedPrefs(context);
        if (editor== null)
            editor = sharedPreferences.edit();
    }


    public static String convertCalGoalToJson(SignupPojo list)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<SignupPojo>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    public static SignupPojo convertJsonToCalGoal(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<SignupPojo>() {}.getType();
        SignupPojo fromJson = gson.fromJson(json, type);
        return fromJson;
    }

    public static String convertDonationToJson(RecordDonationPojo list)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<RecordDonationPojo>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    public static RecordDonationPojo convertJsonToDonation(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<RecordDonationPojo>() {}.getType();
        RecordDonationPojo fromJson = gson.fromJson(json, type);
        return fromJson;
    }

    public static String getStringPref(Context context,String key,String value)
    {
        setSharedPrefs(context);
        return sharedPreferences.getString(key,value);
    }

}
