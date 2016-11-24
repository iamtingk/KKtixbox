package com.iamtingk.kktixbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.iamtingk.kktixbox.models.KKtixboxModel;
import com.iamtingk.kktixbox.notification.services.RegistrationAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {
    private String senderID="1013167703268";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        KKtixboxModel KKT = new KKtixboxModel();




        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null || !info.isAvailable()){
            Log.e("info","無網路");
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this,MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("net_status","請開啟網路後，下拉更新");
                intent.putExtras(bundle);

                startActivity(intent);
                WelcomeActivity.this.finish();




        }else {
            if (KKT.kklist() !=null){
                KKtixboxModel.truncate(KKtixboxModel.class);
            }
            Log.e("info","有網路");
            new GetDataSync().execute();
            new RegistrationAsyncTask(WelcomeActivity.this).execute(senderID);
        }


    }

    public class GetDataSync extends AsyncTask<String,Integer,String> {
        //HttpURLConnection要指定null
        private HttpURLConnection httpURLConn = null;
        private ProgressDialog progressD;
        private StringBuffer result;
        private URL url;
        private BufferedReader bufferRd;
        private String line;
        String json_updated;


        //一開始還沒進到doInBackground時，執行
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        //異步主要工作區
        @Override
        protected String doInBackground(String... params) {
            double resultstart= System.currentTimeMillis();
            result = new StringBuffer();
            result.trimToSize();

            try {

                url = new URL("https://kktix.com/events.json");
                httpURLConn = (HttpURLConnection)url.openConnection();
                bufferRd = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));

                    while ((line = bufferRd.readLine()) !=null){
                        result.append(line);
                    }


                    double resultstop= System.currentTimeMillis();
                    double resulttime=(resultstop-resultstart)/1000;

                    Log.e("連線後寫入strbuf","共花"+String.valueOf(resulttime)+"秒");
                    bufferRd.close();

            }catch (Exception e){
                e.printStackTrace();
            }finally{
                httpURLConn.disconnect();
            }

//            Active Android 寫入

            ActiveAndroid.beginTransaction();

            try {
                double jsonstart= System.currentTimeMillis();
                json_updated = new JSONObject(result.toString()).getString("updated");

                Log.e("json_updated","json更新時間："+json_updated);
                int jsoncount = new JSONArray(new JSONObject(result.toString()).getString("entry")).length();
                Log.e("count","共"+String.valueOf(jsoncount)+"筆json資料");
                String[] json_url = new String[jsoncount];
                String[] json_published = new String[jsoncount];
                String[] json_title = new String[jsoncount];
                String[] json_summary = new String[jsoncount];
                String[] json_content = new String[jsoncount];
                String[] json_author_name = new String[jsoncount];
                String[] json_author_uri = new String[jsoncount];


                JSONArray jsayurl = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsaypublished = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsaytitle = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsaysummary = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsaycontent = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsayauthor_name = new JSONArray(new JSONObject(result.toString()).getString("entry"));
                JSONArray jsayauthor_uri = new JSONArray(new JSONObject(result.toString()).getString("entry"));


                for (int i = 0; i < jsoncount; i++) {

                    json_url[i]= jsayurl.getJSONObject(i).getString("url");
                    json_published[i]= jsaypublished.getJSONObject(i).getString("published");
                    json_title[i]= jsaytitle.getJSONObject(i).getString("title");
                    json_summary[i]= jsaysummary.getJSONObject(i).getString("summary");
                    json_content[i]= jsaycontent.getJSONObject(i).getString("content");
                    json_author_name[i]=new JSONObject(jsayauthor_name.getJSONObject(i).getString("author")).getString("name");
                    json_author_uri[i]=new JSONObject(jsayauthor_uri.getJSONObject(i).getString("author")).getString("uri");

//                    Active Android 寫入
                    KKtixboxModel kktixbox = new KKtixboxModel();
                    kktixbox.kid = i;
                    kktixbox.url = json_url[i];
                    kktixbox.published = json_published[i];
                    kktixbox.title = json_title[i];
                    kktixbox.summary = json_summary[i];
                    kktixbox.content = json_content[i];
                    kktixbox.author_name = json_author_name[i];
                    kktixbox.author_uri = json_author_uri[i];
                    kktixbox.save();
                }
                ActiveAndroid.setTransactionSuccessful();

                double jsonstop= System.currentTimeMillis();
                double jsontime= (jsonstart-jsonstop)/1000;
                Log.e("jsontime","解析json共花"+String.valueOf(jsontime)+"秒");



                return null;

            }catch (JSONException e){
                e.printStackTrace();
                //Active Android 寫入
            }finally {
                ActiveAndroid.endTransaction();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("完成","OK");
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this,MainActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("net_status","資料已更新至最新");
            intent.putExtras(bundle);

            startActivity(intent);
            WelcomeActivity.this.finish();
//
        }


    }


}
