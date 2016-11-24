package com.iamtingk.kktixbox;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.iamtingk.kktixbox.main_fragment.FragmentA;
import com.iamtingk.kktixbox.main_fragment.FragmentB;
import com.iamtingk.kktixbox.main_fragment.FragmentC;
import com.iamtingk.kktixbox.models.KKbuyrecord;
import com.iamtingk.kktixbox.models.KKfavorite;
import com.iamtingk.kktixbox.models.KKtixboxModel;
import com.iamtingk.kktixbox.models.Myuser;
import com.iamtingk.kktixbox.sms_login.SmsOne;
import com.iamtingk.kktixbox.sms_login.SmsTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FragmentA.FragmentInteraction,FragmentC.FragmentInteraction {
    private CharSequence DrawerTitle_main;
    private CharSequence Title_main;
    private DrawerLayout drawer_layout_main;
    private FrameLayout frame_layout_main;
    private ListView listView_main;
    private ActionBarDrawerToggle actionBarDrawerToggle_main;
    private SwipeRefreshLayout swipe_layout;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private View fmA_view;
    private View fmC_view;
    private String[] drawer_item_title;
    private Boolean pap = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent();
//        Log.e("M","1");
            if (getIntent().getExtras().getString("net_status") == null){
                Log.e("1","null");
            }else {
                Log.e("2","null");
                String intent_status = getIntent().getExtras().getString("net_status");
                Toast.makeText(MainActivity.this, intent_status, Toast.LENGTH_SHORT).show();
            }


            initview();//初始化元件
            initSetActionBar();//設置ActionBar啟用
            initDrawerLayout();//初始化DrawerLayout並同步狀態 設定連接ActionBarDrawerToggle監聽 設置fragment後返回鍵
            initDrawerList();//主要設定ListView的監聽跟動作
            initViewPager();
   }
    private void initview() {
        frame_layout_main = (FrameLayout)findViewById(R.id.frame_layout_main);
        drawer_layout_main = (DrawerLayout)findViewById(R.id.drawer_layout_main);
        listView_main = (ListView)findViewById(R.id.listview_main);
        actionBarDrawerToggle_main = new ActionBarDrawerToggle(this,drawer_layout_main,R.string.drawer_open,R.string.drawer_close);
        tab_layout = (TabLayout)findViewById(R.id.tab_layout_main);
        viewpager = (ViewPager)findViewById(R.id.viewpager_main);
    }

    private void initSetActionBar() {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initDrawerLayout() {
        Title_main = DrawerTitle_main = getTitle();

        //actionbar狀態同步
        actionBarDrawerToggle_main.syncState();

        //設置drawer_layout_main監聽 等同連接actionBarDrawerToggle_main
        drawer_layout_main.setDrawerListener(actionBarDrawerToggle_main);


        //控制Fragment的返回箭頭，關鍵在onBackStackChanged
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount()>0){
                    actionBarDrawerToggle_main.setDrawerIndicatorEnabled(false);
                }else{
                    actionBarDrawerToggle_main.setDrawerIndicatorEnabled(true);
                }
            }
        });


     }

    private void initDrawerList() {
        //設置values/strings.xml => string-array
        drawer_item_title = getResources().getStringArray(R.array.drawer_item_title_main);
        if (Myuser.userif()==null){
            drawer_item_title[1]="註冊/登入";
        }else {
            drawer_item_title[1]="登出";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.main_drawer_item,drawer_item_title);
        listView_main.setAdapter(adapter);
        listView_main.setOnItemClickListener(new DrawerItemClickListener());
    }

//    listView_main的Item監聽類別
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("selectItem",String.valueOf(position));
            selectItem(position);
        }
    }

    private void selectItem(int position){
        //android.app類
        Fragment fragment = null;

        //哪個Item被選擇
        switch (position){
            case 0:
                fragment = new About();
                Log.e("position",String.valueOf(position));
                break;
            case 1:
//                沒資料進去，有資料刪掉
                if(Myuser.userif()==null){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,SmsOne.class);
                    startActivity(intent);
                    Log.e("position",String.valueOf(position));

                }else {
                    Myuser.truncate(Myuser.class);
                    drawer_item_title[1]="註冊/登入";
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.main_drawer_item,drawer_item_title);
                    adapter.notifyDataSetChanged();
                    listView_main.setAdapter(adapter);
                    Toast.makeText(MainActivity.this, "已登出！", Toast.LENGTH_SHORT).show();


                }
                return;
            default:
                return;

        }

        //被選擇的Item做什麼事

            FragmentManager fragmentManager = getFragmentManager();

            //android.app類
            //產生Fragment事務
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //跟main.xml的frame_layout_main調換位置
            fragmentTransaction.replace(R.id.frame_layout_main,fragment,"fm"+String.valueOf(position));
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

            listView_main.setItemChecked(position,true);

            //設置values/strings.xml => string-array，等同於drawer_item_title
            String[] drawer_menu_main = this.getResources().getStringArray(R.array.drawer_item_title_main);

            //設置標頭名稱（複寫）
            setTitle(drawer_menu_main[position]);

            //選擇完後，關閉listView_main
            drawer_layout_main.closeDrawer(listView_main);

    }

    //複寫設定標頭名稱
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        Title_main = title;
        getSupportActionBar().setTitle(Title_main);
    }


    private void initViewPager(){
        viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.tablayout_item_title_main)));

        //設定初始出現 0 , (1) , 2頁
        viewpager.setCurrentItem(1);

        //tablayout連接viewpager
        tab_layout.setupWithViewPager(viewpager);


        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        viewpager.setCurrentItem(0);
                        Log.e("onTabSelected","追蹤");
                        List<KKfavorite> KFA = KKfavorite.getFavorite();
                        RecyclerView rere = (RecyclerView)findViewById(R.id.recycleview_fm_a);
                        FragmentA.ContentA adapterA = new FragmentA.ContentA(KFA, fmA_view);
                        rere.swapAdapter(adapterA,false);
                        Log.e("ododod",String.valueOf(KFA.size()));
                        break;
                    case 1:
                        viewpager.setCurrentItem(1);
                        Log.e("onTabSelected","活動");
                        break;
                    case 2:
                        viewpager.setCurrentItem(2);
                        Log.e("onTabSelected","購買紀錄");
                        RecyclerView recyclerView_fm_c = (RecyclerView)findViewById(R.id.recycleview_fm_c);
                        List<KKbuyrecord> buylist = KKbuyrecord.getbuyList();
                        FragmentC.ContentC adapterC = new FragmentC.ContentC(buylist, fmC_view);
                        recyclerView_fm_c.swapAdapter(adapterC,false);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    //FragmentPagerAdapter是v4的方法，但仍然可以用在新的Fragment
    public class FragmentAdapter extends FragmentPagerAdapter{
        private String[] tab_item_title;
        public FragmentAdapter(android.support.v4.app.FragmentManager fm,String[] tab_item_title) {
            super(fm);
            this.tab_item_title = tab_item_title;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentA();
                case 1:
                    return new FragmentB();
                case 2:
                    return new FragmentC();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tab_item_title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab_item_title[position];
        }
    }

    //ActionBar選擇項目
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //當actionBarDrawerToggle_main被選擇 -> return true
        if (actionBarDrawerToggle_main.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void process(View view) {
        if(view != null){
            this.fmA_view = view;
        }
    }

    @Override
    public void getViewC(View view) {
        if(view != null){
            this.fmC_view = view;
        }
    }
}
