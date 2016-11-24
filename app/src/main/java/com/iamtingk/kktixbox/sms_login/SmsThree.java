package com.iamtingk.kktixbox.sms_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iamtingk.kktixbox.MainActivity;
import com.iamtingk.kktixbox.R;

public class SmsThree extends AppCompatActivity {
    private Button btn_sms_sms03;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_three);
        btn_sms_sms03 = (Button)findViewById(R.id.btn_sms_sms03);
        btn_sms_sms03.setOnClickListener(smsto03);
    }
    private  Button.OnClickListener smsto03 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(SmsThree.this,MainActivity.class);
            startActivity(intent);

        }
    };
}
