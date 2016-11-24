package com.iamtingk.kktixbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    private TextView txt_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        txt_notification = (TextView)findViewById(R.id.txt_notification);
        String bundle = getIntent().getExtras().getString("messageon");
        txt_notification.setText(bundle);
    }
}
