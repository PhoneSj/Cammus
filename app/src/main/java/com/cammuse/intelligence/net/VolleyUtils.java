package com.cammuse.intelligence.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cammuse.intelligence.MyApplication;
import com.cammuse.intelligence.utils.ConstantUtils;

import org.json.JSONObject;


/**
 * Created by Administrator on 2016/2/22.
 */
public class VolleyUtils {
    protected static final String TAG = "VolleyUtils";

    /**
     * 核对用户名和密码
     */
//    public static void checkUsernameAndPassword(Context mContext, String url,
//                                                final Handler mHandler) {
//
//        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Message msg = Message.obtain();
//                msg.what = ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_LOGIN;
//                msg.obj = response;
//                mHandler.sendMessage(msg);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mHandler.sendEmptyMessage(ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE);
//                Log.e(TAG, error.toString());
//            }
//        });
//        MyApplication.getRequestQueue(mContext).add(mJsonObjectRequest);
//    }


    /**
     * 请求发送短信验证码
     */
    public static void requestSendCode(Context mContext, String url,
                                       final Handler mHandler) {

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Message msg = Message.obtain();
                msg.what = ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SENDCODE;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessage(ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE);
                Log.e(TAG, error.toString());
            }
        });
        MyApplication.getRequestQueue(mContext).add(mJsonObjectRequest);
    }

    /**
     * 请求核对短信验证码
     */
    public static void requestCheckCode(Context mContext, String url,
                                        final Handler mHandler) {

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Message msg = Message.obtain();
                msg.what = ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_CHECKCODE;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessage(ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE);
                Log.e(TAG, error.toString());
            }
        });
        MyApplication.getRequestQueue(mContext).add(mJsonObjectRequest);
    }

    /**
     * 提交用户名和密码
     */
    public static void submitUsernameAndPassword(Context mContext, String url,
                                                 final Handler mHandler) {

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Message msg = Message.obtain();
                msg.what = ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessage(ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE);
                Log.e(TAG, error.toString());
            }
        });
        MyApplication.getRequestQueue(mContext).add(mJsonObjectRequest);
    }

}
