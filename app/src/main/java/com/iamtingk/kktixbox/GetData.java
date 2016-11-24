package com.iamtingk.kktixbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.iamtingk.kktixbox.main_fragment.FragmentB;
import com.iamtingk.kktixbox.models.KKtixboxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by tingk on 2016/6/15.
 */

public class GetData extends AsyncTask<String, String, String> {
    //HttpURLConnection要指定null
    private HttpURLConnection httpURLConn = null;
    private ProgressDialog progressD;
    private StringBuffer result;
    private URL url;
    private BufferedReader bufferRd;
    private String line;
    public Context context;
    public FragmentB.ContactsAdapter adapter;
    public View view;

    public RecyclerView recyclerView_fm_b;
    public SwipeRefreshLayout swipe_layout_fm_b;
    private List<KKtixboxModel> kklist;
    String json_updated;

    public GetData(final Context context, final FragmentB.ContactsAdapter adapter,final View view,final RecyclerView recyclerView_fm_b,final SwipeRefreshLayout swipe_layout_fm_b) {
        this.context = context;
        this.adapter = adapter;
        this.view = view;
        this.recyclerView_fm_b = recyclerView_fm_b;
        this.swipe_layout_fm_b = swipe_layout_fm_b;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        result = new StringBuffer();
        result.trimToSize();
        try {
            double resultstart= System.currentTimeMillis();
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
        double sqlstart= System.currentTimeMillis();
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
                kktixbox.published = json_updated;
                kktixbox.title = json_title[i];
                kktixbox.summary = json_summary[i];
                kktixbox.content = json_content[i];
                kktixbox.author_name = json_author_name[i];
                kktixbox.author_uri = json_author_uri[i];
                kktixbox.save();
            }
            ActiveAndroid.setTransactionSuccessful();

            double jsonstop= System.currentTimeMillis();
            double sqlstop= System.currentTimeMillis();
            double jsontime= (jsonstop - jsonstart)/1000;
            Log.e("jsontime","解析json共花"+String.valueOf(jsontime)+"秒");

            double sqltime=(sqlstop-sqlstart)/1000;
            Log.e("jsontime","解析json＋寫入SQL共花"+String.valueOf(sqltime)+"秒");

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
        kklist = KKtixboxModel.kklist();
        adapter = new FragmentB.ContactsAdapter(kklist,view);
        recyclerView_fm_b.swapAdapter(adapter,false);
        swipe_layout_fm_b.setRefreshing(false);
        Toast.makeText(view.getContext(), "已更新到最新!", Toast.LENGTH_SHORT).show();
    }
}
