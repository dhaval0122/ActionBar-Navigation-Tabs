package com.djandroid.jdroid.materialdesign;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.WindowManager;


/**
 * Created by dhawal sodha parmar on 4/29/2015.
 */



public class MyApplication extends Application {


    @Override
    public void onCreate() {



        super.onCreate();
    }


    public static boolean isLollipop(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return true;
        }
        return false;
    }

}
