package com.iamtingk.kktixbox.sms_login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.iamtingk.kktixbox.MainActivity;
import com.iamtingk.kktixbox.R;
import com.iamtingk.kktixbox.models.Myuser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsTwo extends AppCompatActivity {
    private Button btn_sms_sms02;
    private TextView txt_sms_sms02;
    private EditText edt_sms_sms02;
    private String str_pdus_in="";
    private int verify_count=1;
    private final static String MSG_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private String usersms="";
    private String userphone="";
    private StringBuffer result;
    Handler handler;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_two);

        btn_sms_sms02 = (Button)findViewById(R.id.btn_sms_sms02);
        btn_sms_sms02.setOnClickListener(smsto02);
        txt_sms_sms02 = (TextView)findViewById(R.id.txt_sms_sms02);
        edt_sms_sms02 = (EditText)findViewById(R.id.edt_sms_sms02);
        registerReceiver(broadcastReceiver,new IntentFilter(MSG_RECEIVED));
        phone = getIntent().getExtras().getString("userphone");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 999:
//                        txt_01_two.setText("Token碼："+result.toString()+"\n\n"+"手機號碼："+userphone+"\n\n"+"驗證碼"+usersms);

                        if (Myuser.getuser(userphone)==null) {

                            ActiveAndroid.beginTransaction();
                            try {
                                Myuser myuser = new Myuser();
                                myuser.phone = userphone;
                                myuser.sms_verify = usersms;
                                myuser.sms_token = result.toString();
                                myuser.save();
                                ActiveAndroid.setTransactionSuccessful();

                            } finally {
                                ActiveAndroid.endTransaction();
                            }

                        }else {
                            Toast.makeText(SmsTwo.this, "登入成功！", Toast.LENGTH_SHORT).show();
                        }
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent();
                        bundle.putString("phone",userphone);
                        intent.putExtras(bundle);
                        intent.setClass(SmsTwo.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SmsTwo.this, "登入成功！", Toast.LENGTH_SHORT).show();
                        SmsTwo.this.finish();

                        break;
                }
            }
        };



    }
    private  Button.OnClickListener smsto02 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(edt_sms_sms02.getText().length()>6&&edt_sms_sms02.getText().length()<8) {

                if (!(str_pdus_in==null)) {
                    Log.e("str_pdus_in",str_pdus_in);
                    if (edt_sms_sms02.getText().toString().equals(str_pdus_in)) {
                        Log.e("edt_sms_sms02", edt_sms_sms02.getText().toString());
                        Log.e("str_pdus_in", str_pdus_in);
//                        Toast.makeText(SmsTwo.this, "驗證OK", Toast.LENGTH_SHORT).show();
                        usersms=str_pdus_in;
                        userphone=phone;

                        Thread thread01 = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                SendTwoVerify();
                                Log.e("sss",result.toString());
                                Message m =new Message();
                                m.what=999;
                                handler.sendMessage(m);
                            }
                        };
                        thread01.start();



                    } else {
                        if (verify_count<5) {
                            txt_sms_sms02.setText("驗證碼有誤，還可以驗證"+(5-verify_count)+"次");
                            verify_count++;

                        }else {
                            Toast.makeText(SmsTwo.this, "請重新操作", Toast.LENGTH_SHORT).show();
                            SmsTwo.this.finish();
                        }


                    }
                } else {
                    txt_sms_sms02.setText("驗證碼有誤，請重新操作");
                }
            }else {
                txt_sms_sms02.setText("請正確輸入七個字");
            }


        }
    };


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage sms;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String format = bundle.getString("format");
                sms = SmsMessage.createFromPdu((byte[])pdus[0],format);
                Log.e("SMS-onReceive", ">=M");
            }else {
                sms = SmsMessage.createFromPdu((byte[])pdus[0]);
                Log.e("SMS-onReceive", "<M");
            }


            String str_pdus;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Form:"+sms.getDisplayOriginatingAddress()+"\n");
            stringBuffer.append("Body:"+sms.getDisplayMessageBody());
            str_pdus=sms.getDisplayMessageBody();

            txt_sms_sms02.setText(stringBuffer);
            unregisterReceiver(broadcastReceiver);
            str_pdus_in = str_pdus.split("驗證碼:")[1];
            edt_sms_sms02.setText(str_pdus_in);


            //收到驗證碼後，自動點擊登入
//            btn_sms_sms02.performClick();

        }
    };


    private void SendTwoVerify(){
//        String twourl = "http://54.169.142.221:8082/two?user_sms="+usersms+"&user_phone="+userphone;
        String twourl = "http://52.220.111.40:18081/two?user_sms="+usersms+"&user_phone="+userphone;

        Log.e("twourl",twourl);
        URL url;
        String line;
        try {
            result = new StringBuffer();
            url = new URL(twourl);
            HttpURLConnection httpurlconn = (HttpURLConnection)url.openConnection();
            httpurlconn.setRequestMethod("GET");
            InputStream is = httpurlconn.getInputStream();
            InputStreamReader isRead = new InputStreamReader(is);
            BufferedReader bufferRead = new BufferedReader(isRead);
            while ((line = bufferRead.readLine())!=null){
                result.append(line);
            }
            bufferRead.close();
            isRead.close();
            is.close();
            httpurlconn.disconnect();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
