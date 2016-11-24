package com.iamtingk.kktixbox;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.iamtingk.kktixbox.R;

/**
 * Created by tingk on 2016/6/11.
 */
public class About extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listitem_about,container,false);
        Log.e("fmfm",getFragmentManager().findFragmentByTag("fm0").toString());
        //Fragment為About之後，返回鍵有作用
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return false;
    }
}
