package com.iamtingk.kktixbox;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tingk on 2016/6/11.
 */
public class Application extends android.app.Application {
    private RequestQueue myRequestQueue;
    private String TAG = "GCM Sample Client";
    private static Application myIntsence;
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        myIntsence = this;//    Volley Instence
    }

    //    Volley start
    public static synchronized Application getInstence(){
//        Log.e("myIntsence",myIntsence.toString());
        return myIntsence;
    }

    public RequestQueue getRequestQueue(){
//        Log.e("myRequestQueue1",myRequestQueue.toString());
        if (myRequestQueue==null){
            myRequestQueue = Volley.newRequestQueue(getApplicationContext());
//            Log.e("myRequestQueue2",myRequestQueue.toString());
        }
        return myRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
//        Log.e("Request1",req.toString());
        req.setTag(TAG);
//        Log.e("Request2",req.toString());
//        Log.e("getRequestQueue1",getRequestQueue().toString());
        getRequestQueue().add(req);
//        Log.e("getRequestQueue2",getRequestQueue().toString());
    }
//    Volley end


}
