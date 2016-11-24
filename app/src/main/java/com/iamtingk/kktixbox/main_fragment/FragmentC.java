package com.iamtingk.kktixbox.main_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamtingk.kktixbox.ContentActivity;
import com.iamtingk.kktixbox.MyDecoration;
import com.iamtingk.kktixbox.R;
import com.iamtingk.kktixbox.models.KKbuyrecord;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tingk on 2016/6/11.
 */
public class FragmentC extends Fragment {
    private RecyclerView recyclerView_fm_c;
    private List<KKbuyrecord> kklist;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_c, container, false);
        listternerC.getViewC(view);
        recyclerView_fm_c = (RecyclerView) view.findViewById(R.id.recycleview_fm_c);
        kklist = KKbuyrecord.getbuyList();
        ContentC adapter = new ContentC(kklist, view);
        recyclerView_fm_c.setAdapter(adapter);
        recyclerView_fm_c.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView_fm_c.setHasFixedSize(true);
        recyclerView_fm_c.addItemDecoration(new MyDecoration(view.getContext(), MyDecoration.VERTICAL_LIST));
        return view;
    }

    public static class ContentC extends RecyclerView.Adapter<ContentC.HolderC> {
        public List<KKbuyrecord> kklist;
        public View view;

        public ContentC(List<KKbuyrecord> contact, View view) {
            kklist = contact;
            this.view = view;
        }

        @Override
        public HolderC onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.fm_c_recy_item, parent, false);
            HolderC holderC = new HolderC(contactView);
            return holderC;
        }

        @Override
        public void onBindViewHolder(HolderC holder, int position) {
            KKbuyrecord contact = kklist.get(position);
            TextView fm_c_item_title = holder.fm_c_item_title;
            TextView fm_c_item_time = holder.fm_c_item_time;
            String time = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss").format(contact.created_time);
            fm_c_item_title.setText(contact.title);
            fm_c_item_time.setText(time);
            ImageView fm_c_item_imgview = holder.fm_c_item_imgview;
            fm_c_item_imgview.setImageResource(R.mipmap.y3);
        }

        @Override
        public int getItemCount() {
            return kklist.size();
        }

        public class HolderC extends RecyclerView.ViewHolder {
            private TextView fm_c_item_title, fm_c_item_time;
            private ImageView fm_c_item_imgview;

            public HolderC(final View itemView) {
                super(itemView);
                fm_c_item_title = (TextView) itemView.findViewById(R.id.fm_c_item_title);
                fm_c_item_time = (TextView) itemView.findViewById(R.id.fm_c_item_time);
                fm_c_item_imgview = (ImageView) itemView.findViewById(R.id.fm_c_item_imgview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (KKbuyrecord.eq(fm_c_item_title.getText().toString()) != null) {
                            if (KKbuyrecord.eq(fm_c_item_title.getText().toString()).title.equals(fm_c_item_title.getText().toString())) {
                                String uniconC = KKbuyrecord.eq(fm_c_item_title.getText().toString()).url;
                                Uri uri = Uri.parse(uniconC);
                                Intent intenturi = new Intent(Intent.ACTION_VIEW, uri);
                                view.getContext().startActivity(intenturi);
                            } else {
                                Log.e("kkbuy", "null");
                            }
                        } else {
                            Log.e("kkbuyC", "null");
                        }

                    }
                });

            }
        }

    }

    private FragmentInteraction listternerC;
    public FragmentC() {
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listternerC = (FragmentInteraction) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listternerC = null;
    }

    public interface FragmentInteraction {
        void getViewC(View view);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buyrecord, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_fmC:
                KKbuyrecord.deletebuy();
                kklist = KKbuyrecord.getbuyList();
                ContentC adapter = new ContentC(kklist, view);
                recyclerView_fm_c.swapAdapter(adapter, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

