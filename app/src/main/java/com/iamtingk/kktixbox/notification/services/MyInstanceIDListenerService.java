package com.iamtingk.kktixbox.notification.services;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by tingk on 2016/7/10.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        new RegistrationAsyncTask(null).execute();
    }
}
