package com.iamtingk.kktixbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamtingk.kktixbox.main_fragment.FragmentA;
import com.iamtingk.kktixbox.main_fragment.FragmentB;
import com.iamtingk.kktixbox.main_fragment.FragmentC;
import com.iamtingk.kktixbox.models.KKbuyrecord;
import com.iamtingk.kktixbox.models.KKfavorite;
import com.iamtingk.kktixbox.models.KKtixboxModel;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;

public class ContentActivity extends AppCompatActivity{
    private TextView txt_url_content, txt_published_content, txt_title_content, txt_summary_content, txt_content_content, txt_author_name_content, txt_author_uri_content;
    private Button btn_favorite_content, btn_buy_content;
    private String fromFragment;
    private KKtixboxModel KKcontentB;
    private KKfavorite kKfavoriteA,kkfavoritetitleA,kKfavoriteB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
        Bundle bundle = getIntent().getExtras();
        fromFragment = bundle.getString("Fragment");
        switch (fromFragment){
            case "A":
                String titleA = bundle.getString("title");
                initviewA(titleA);
                Log.e("titleA",titleA);
                break;
            case "B":
                String titleB = bundle.getString("title");
                Log.e("titlee",titleB);
                initviewB(titleB);
                break;
        }
        btn_buy_content.setOnClickListener(click_buy);
    }
    private void init(){
        txt_url_content = (TextView) findViewById(R.id.txt_url_content);
        txt_published_content = (TextView) findViewById(R.id.txt_published_content);
        txt_title_content = (TextView) findViewById(R.id.txt_title_content);
        txt_summary_content = (TextView) findViewById(R.id.txt_summary_content);
        txt_content_content = (TextView) findViewById(R.id.txt_content_content);
        txt_author_name_content = (TextView) findViewById(R.id.txt_author_name_content);
        txt_author_uri_content = (TextView) findViewById(R.id.txt_author_uri_content);
        btn_favorite_content = (Button) findViewById(R.id.btn_favorite_content);
        btn_buy_content = (Button) findViewById(R.id.btn_buy_content);
    }

    private void initviewA(String title){
        kkfavoritetitleA = KKfavorite.getFavoritetitle(title);
        kKfavoriteA = new KKfavorite();
        txt_url_content.setText(kkfavoritetitleA.url);
        txt_published_content.setText(kkfavoritetitleA.published);
        txt_title_content.setText(kkfavoritetitleA.title);
        txt_summary_content.setText(kkfavoritetitleA.summary);
        txt_content_content.setText(kkfavoritetitleA.content);
        txt_author_name_content.setText(kkfavoritetitleA.author_name);
        txt_author_uri_content.setText(kkfavoritetitleA.author_uri);

        if ((kKfavoriteA.eqFa(txt_title_content.getText().toString())) != null) {
            if ((kKfavoriteA.eqFa(txt_title_content.getText().toString()).title).equals(txt_title_content.getText().toString())) {
                btn_favorite_content.setText("已加入");
            }
        } else {
            btn_favorite_content.setText("加入最愛");
        }

        btn_favorite_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((kKfavoriteA.eqFa(txt_title_content.getText().toString())) != null) {
                    if ((kKfavoriteA.eqFa(txt_title_content.getText().toString()).title).equals(txt_title_content.getText().toString())) {
                        kKfavoriteA.KKdelete(txt_title_content.getText().toString());
                        btn_favorite_content.setText("加入最愛");
                    }
                } else {
                    KKfavorite kkfavorite = new KKfavorite();
                    if (kKfavoriteA.getId()==null){
                        kkfavorite.fid = 0;
                    }else {
                        kkfavorite.fid = Long.valueOf(kKfavoriteA.getId()+1);
                    }
                    kkfavorite.url = txt_url_content.getText().toString();
                    kkfavorite.published = txt_published_content.getText().toString();
                    kkfavorite.title = txt_title_content.getText().toString();
                    kkfavorite.summary = txt_summary_content.getText().toString();
                    kkfavorite.content = txt_content_content.getText().toString();
                    kkfavorite.author_name = txt_author_name_content.getText().toString();
                    kkfavorite.author_uri = txt_author_uri_content.getText().toString();
                    kkfavorite.created_time = new Date().getTime();
                    kkfavorite.save();
                    btn_favorite_content.setText("已加入");
                }
            }
        });
    }

    private void initviewB(String titleB) {
        KKcontentB = KKtixboxModel.KKcontent(titleB);
        kKfavoriteB = new KKfavorite();
        txt_url_content.setText(KKcontentB.url);
        txt_published_content.setText(KKcontentB.published);
        txt_title_content.setText(KKcontentB.title);
        txt_summary_content.setText(KKcontentB.summary);
        txt_content_content.setText(KKcontentB.content);
        txt_author_name_content.setText(KKcontentB.author_name);
        txt_author_uri_content.setText(KKcontentB.author_uri);
        if ((kKfavoriteB.eqFa(txt_title_content.getText().toString())) != null) {
            if ((kKfavoriteB.eqFa(txt_title_content.getText().toString()).title).equals(txt_title_content.getText().toString())) {
                btn_favorite_content.setText("已加入");
            }
        } else {
            btn_favorite_content.setText("加入最愛");
        }
        btn_favorite_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((kKfavoriteB.eqFa(txt_title_content.getText().toString())) != null) {
                    if ((kKfavoriteB.eqFa(txt_title_content.getText().toString()).title).equals(txt_title_content.getText().toString())) {
                        kKfavoriteB.KKdelete(txt_title_content.getText().toString());
                        btn_favorite_content.setText("加入最愛");
                    }
                } else {
                    KKfavorite kkfavorite = new KKfavorite();
                    if (kKfavoriteB.getId()==null){
                        kkfavorite.fid = 0;
                    }else {
                        kkfavorite.fid = Long.valueOf(kKfavoriteB.getId()+1);
                    }
                    kkfavorite.url = txt_url_content.getText().toString();
                    kkfavorite.published = txt_published_content.getText().toString();
                    kkfavorite.title = txt_title_content.getText().toString();
                    kkfavorite.summary = txt_summary_content.getText().toString();
                    kkfavorite.content = txt_content_content.getText().toString();
                    kkfavorite.author_name = txt_author_name_content.getText().toString();
                    kkfavorite.author_uri = txt_author_uri_content.getText().toString();
                    kkfavorite.created_time = new Date().getTime();
                    kkfavorite.save();
                    btn_favorite_content.setText("已加入");
                }
            }
        });
    }



    Button.OnClickListener click_buy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("urlu",txt_url_content.getText().toString());
            KKbuyrecord kkbut = new KKbuyrecord();
            if (KKbuyrecord.eq(txt_published_content.getText().toString()) !=null){
                if (KKbuyrecord.eq(txt_published_content.getText().toString()).title.equals(txt_published_content.getText().toString())){
                }else {
                    if (kkbut.getId()==null){
                        kkbut.bid = 0;
                    }else {
                        kkbut.bid = Long.valueOf(kkbut.getId()+1);
                    }
                    kkbut.url = txt_url_content.getText().toString();
                    kkbut.published = txt_published_content.getText().toString();
                    kkbut.title = txt_title_content.getText().toString();
                    kkbut.created_time = new Date().getTime();
                    kkbut.save();
                }
            }else {
                if (kkbut.getId()==null){
                    kkbut.bid = 0;
                }else {
                    kkbut.bid = Long.valueOf(kkbut.getId()+1);
                }
                kkbut.url = txt_url_content.getText().toString();
                kkbut.published = txt_published_content.getText().toString();
                kkbut.title = txt_title_content.getText().toString();
                kkbut.created_time = new Date().getTime();
                kkbut.save();
            }
            String uricon = txt_url_content.getText().toString();
            Uri uri = Uri.parse(uricon);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            ContentActivity.this.startActivity(intent);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            ContentActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ContentActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
