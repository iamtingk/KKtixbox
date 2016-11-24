package com.iamtingk.kktixbox.notification.services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.iamtingk.kktixbox.Application;
import com.iamtingk.kktixbox.models.Myuser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tingk on 2016/7/10.
 */
public class RegistrationAsyncTask extends AsyncTask<String, Void, Void> {
    private static String KEY_TOKEN_SENT_TO_SEVER = "tokenSentToServer";
    private static String APPLICATION_SERVER_URL = "http://52.220.111.40:18081/gcm/get_token";
//    private static String APPLICATION_SERVER_URL = "111.252.40.242:3000/gcm/get_token";
    private Activity myactivity;
    private String token;


    public RegistrationAsyncTask(Activity activity) {
        this.myactivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(myactivity);
        String senderID = params[0];

        try {
            InstanceID instanceID = InstanceID.getInstance(myactivity);
            token = instanceID.getToken(senderID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            sendTokenToServer();
//            Myuser myuser = new Myuser();
//            存入myuser
//

            sharedPreferences.edit().putBoolean(KEY_TOKEN_SENT_TO_SEVER,true).apply();
        }catch (Exception e){
            sharedPreferences.edit().putBoolean(KEY_TOKEN_SENT_TO_SEVER,false).apply();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("onPostExecute-Token",token);
    }

    private void sendTokenToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, APPLICATION_SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",token);
                Log.e("params",params.toString());
                return params;
            }
        };
        Application.getInstence().addToRequestQueue(request);
    }

}
