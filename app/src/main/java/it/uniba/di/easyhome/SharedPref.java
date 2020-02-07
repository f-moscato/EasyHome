package it.uniba.di.easyhome;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {
    private String LANG_CURRENT="en";
    SharedPreferences mySharedPref;
    public SharedPref(Context context){
        mySharedPref = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    public Boolean loadNightModeState(){
        Boolean state =mySharedPref.getBoolean("NightMode",false);
        return state;
    }
    public void changeLang(String lang){
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putString("Language",lang);
        editor.commit();
    }
    public String loadLang(){
        String lan=mySharedPref.getString("Language","en");
        return lan;
    }
}
