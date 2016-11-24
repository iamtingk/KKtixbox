package com.iamtingk.kktixbox.sms_login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iamtingk.kktixbox.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SmsOne extends AppCompatActivity {
    private Button btn_sms_sms01;
    private EditText edt_sms_sms01;
    private String number_in="";
    private Handler handler;
    private int WAIT_MINS=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_one);
        btn_sms_sms01 = (Button)findViewById(R.id.btn_sms_sms01);
        btn_sms_sms01.setOnClickListener(smsto01);
        edt_sms_sms01 = (EditText)findViewById(R.id.edt_sms_sms01);

        handler = new Main_Handler();
    }
    private  Button.OnClickListener smsto01 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btn_sms_sms01.setEnabled(false);
            number_in = edt_sms_sms01.getText().toString();
            Thread thread_01_main = new Thread(){
                @Override
                public void run() {
                    super.run();
                    minsback();
                }
            };
            thread_01_main.start();
            new Thread(new Thread(){
                @Override
                public void run() {
                    super.run();
                    send();
                }
            }).start();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("userphone",number_in);
            intent.putExtras(bundle);
            intent.setClass(SmsOne.this,SmsTwo.class);
            startActivity(intent);
        }
    };

    private class Main_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int mins = msg.arg1;
            String wait = "等待"+mins+"秒";
            if (mins<WAIT_MINS&&mins!=0){
                btn_sms_sms01.setText(wait);
                btn_sms_sms01.setEnabled(false);
            }else if (mins==0){
                btn_sms_sms01.setText("註冊/登入");
                btn_sms_sms01.setEnabled(true);
            }
        }
    }

    public void minsback(){
        for (int i=WAIT_MINS; i>=0;i--){
            try {
                Message m = new Message();
                m.arg1 = i;
                handler.sendMessage(m);
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void send(){
//        String geturl = "http://54.169.142.221:8082/one?number_to=";
        String geturl = "http://52.220.111.40:18081/one?number_to=";
        URL url;
        try {
            url = new URL(geturl+number_in);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int Codeadd = httpURLConnection.getResponseCode();
            if (Codeadd == 200){
                Log.e("send","OK");
                Log.e("input",httpURLConnection.getInputStream().toString());
            }else {
                Log.e("send","NO");
            }
            httpURLConnection.disconnect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
