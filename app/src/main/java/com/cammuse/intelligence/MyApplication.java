package com.cammuse.intelligence;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 在开发应用时都会和Activity打交道，而Application使用的就相对较少了。
 * Application是用来管理应用程序的全局状态的，比如载入资源文件。
 * 在应用程序启动的时候Application会首先创建，然后才会根据情况(Intent)启动相应的Activity或者Service。
 * 在本文将在Application中注册未捕获异常处理器。
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static RequestQueue getRequestQueue(Context mContext) {

        return Volley.newRequestQueue(mContext);
    }
}