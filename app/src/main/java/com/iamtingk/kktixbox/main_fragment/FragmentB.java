package com.iamtingk.kktixbox.main_fragment;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamtingk.kktixbox.ContentActivity;
import com.iamtingk.kktixbox.GetData;
import com.iamtingk.kktixbox.MyDecoration;
import com.iamtingk.kktixbox.R;

import com.iamtingk.kktixbox.models.KKtixboxModel;

import java.util.List;

/**
 * Created by tingk on 2016/6/11.
 */

public class FragmentB extends Fragment{
    private SwipeRefreshLayout swipe_layout_fm_b;

    private RecyclerView recyclerView_fm_b;
    private List<KKtixboxModel> kklist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_b, container, false);
        recyclerView_fm_b = (RecyclerView) view.findViewById(R.id.recycleview_fm_b);
        kklist = KKtixboxModel.kklist();
        ContactsAdapter adapter = new ContactsAdapter(kklist, view);
        recyclerView_fm_b.setAdapter(adapter);
        recyclerView_fm_b.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //固定高度，可提升效能
        recyclerView_fm_b.setHasFixedSize(true);

        recyclerView_fm_b.addItemDecoration(new MyDecoration(view.getContext(), MyDecoration.VERTICAL_LIST));
        swipe_layout_fm_b = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout_fm_b);
        initswipe_fm_b(view, adapter, recyclerView_fm_b, swipe_layout_fm_b);

        return view;
    }

    private void initswipe_fm_b(final View view, final ContactsAdapter adapter, final RecyclerView recyclerView_fm_b, final SwipeRefreshLayout swipe_layout_fm_b) {
        swipe_layout_fm_b.setSize(SwipeRefreshLayout.LARGE);
        swipe_layout_fm_b.setColorSchemeResources(R.color.kkgreen);
        final Handler handle = new Handler();
        swipe_layout_fm_b.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handle.post(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo info = cm.getActiveNetworkInfo();
                        if (info == null || !info.isAvailable()){
                            //無網路
                            swipe_layout_fm_b.setRefreshing(false);
                            Toast.makeText(view.getContext(), "請開啟網路", Toast.LENGTH_SHORT).show();
                        }else {
                            //有網路
                            //刪除
                            KKtixboxModel.truncate(KKtixboxModel.class);
                            new GetData(view.getContext(), adapter, view, recyclerView_fm_b, swipe_layout_fm_b).execute();
                        }

                    }
                });
            }
        });
    }

    public static class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Holder> {
        public List<KKtixboxModel> kklist;
        public View view;

        public ContactsAdapter(List<KKtixboxModel> contact, View view) {
            kklist = contact;
            this.view = view;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.fm_b_recy_item, parent, false);
            Holder holder = new Holder(contactView);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            KKtixboxModel contact = kklist.get(position);
            TextView fm_b_txt_title = holder.fm_b_item_title;
            TextView fm_b_txt_time = holder.fm_b_item_time;
            ImageView fm_b_item_imgview = holder.fm_b_item_imgview;
            fm_b_item_imgview.setImageResource(R.mipmap.y3);
            String published = contact.published.substring(0, 10);
            fm_b_txt_title.setText(contact.title);
            fm_b_txt_time.setText(published);
        }

        @Override
        public int getItemCount() {
            return kklist.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            private TextView fm_b_item_title, fm_b_item_time;
            private LinearLayout fm_b_item_linear;
            private ImageView fm_b_item_imgview;

            public Holder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("clickItem", String.valueOf(getAdapterPosition() + 1));
                        Toast.makeText(view.getContext(), "第"+String.valueOf(getAdapterPosition() + 1)+"筆", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), ContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Fragment", "B");
                        bundle.putString("title", fm_b_item_title.getText().toString());
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }
                });
                fm_b_item_title = (TextView) itemView.findViewById(R.id.fm_b_item_title);
                fm_b_item_time = (TextView) itemView.findViewById(R.id.fm_b_item_time);
                fm_b_item_linear = (LinearLayout) itemView.findViewById(R.id.fm_b_item_linear);
                fm_b_item_imgview = (ImageView) itemView.findViewById(R.id.fm_b_item_imgview);
            }
        }
    }
}














