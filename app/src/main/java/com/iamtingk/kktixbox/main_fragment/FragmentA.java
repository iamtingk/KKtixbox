package com.iamtingk.kktixbox.main_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamtingk.kktixbox.ContentActivity;
import com.iamtingk.kktixbox.MyDecoration;
import com.iamtingk.kktixbox.R;
import com.iamtingk.kktixbox.models.KKfavorite;

import java.util.List;

/**
 * Created by tingk on 2016/6/11.
 */
public class FragmentA extends Fragment {
    private RecyclerView recyclerView_fm_a;
    private List<KKfavorite> kklist;
    private View view;
    private ContentA adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_a, container, false);
        listterner.process(view);

        kklist = KKfavorite.getFavorite();
        adapter = new ContentA(kklist, view);
        recyclerView_fm_a = (RecyclerView) view.findViewById(R.id.recycleview_fm_a);
        recyclerView_fm_a.setAdapter(adapter);
        recyclerView_fm_a.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView_fm_a.setHasFixedSize(true);
        recyclerView_fm_a.addItemDecoration(new MyDecoration(view.getContext(), MyDecoration.VERTICAL_LIST));
        recyclerView_fm_a.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (e.getAction()){}
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        ItemTouchHelper.Callback mcallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.e("position",String.valueOf(position));
//                new ContentA(kklist,viewHolder.itemView).removeitem(position);
//                KKfavorite item = KKfavorite.load(KKfavorite.class,position);///
//                item.delete();///
//                判斷出位置，重讀資料表取得title，依照where title刪除title的資料，重讀kklist，swapadapter

//                final KKfavorite models = kklist.remove(position);
//                kklist.get(position).delete();


//                kklist.remove(position);
//                kklist.clear();
//                adapter = new ContentA(kklist, view);///
//                adapter.notifyItemRemoved(position);
//                recyclerView_fm_a.getAdapter().notifyDataSetChanged();//
//                recyclerView_fm_a.swapAdapter(adapter,false);///
//                adapter.notifyDataSetChanged();
//                recyclerView_fm_a.setAdapter(adapter);
            }



            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    final float alpha = 1-Math.abs(dX)/(float)viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mcallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_fm_a);
//        setHasOptionsMenu(true);///
        return view;
    }

    public static class ContentA extends RecyclerView.Adapter<ContentA.HolderA> {
        public List<KKfavorite> kklist;
        public View view;

        public ContentA(List<KKfavorite> contact, View view) {
            kklist = contact;
            this.view = view;
        }

        @Override
        public HolderA onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.fm_a_recy_item, parent, false);
            HolderA holderA = new HolderA(contactView);
            return holderA;
        }

        @Override
        public void onBindViewHolder(HolderA holder, int position) {
            KKfavorite contact = kklist.get(position);
            TextView fm_a_item_title = holder.fm_a_item_title;
            TextView fm_a_item_time = holder.fm_a_item_time;
            String published = contact.published.substring(0, 10);
            fm_a_item_title.setText(contact.title);
            fm_a_item_time.setText(published);
            ImageView fm_a_item_imgview = holder.fm_a_item_imgview;
            fm_a_item_imgview.setImageResource(R.mipmap.y3);
        }

        @Override
        public int getItemCount() {
            return kklist.size();
        }
//        public void removeItem(int position){
//            kklist.remove(position);
//            kklist.get(position).delete();
//            notifyItemRemoved(position);
//        }

        public void anti(){

        }

        public KKfavorite removeitem(int position){
            final KKfavorite model = kklist.remove(position);
            notifyItemRemoved(position);
            return model;
        }


        public class HolderA extends RecyclerView.ViewHolder {
            private TextView fm_a_item_title, fm_a_item_time;
            private ImageView fm_a_item_imgview;

            public HolderA(final View itemView) {
                super(itemView);
                fm_a_item_title = (TextView) itemView.findViewById(R.id.fm_a_item_title);
                fm_a_item_time = (TextView) itemView.findViewById(R.id.fm_a_item_time);
                fm_a_item_imgview = (ImageView) itemView.findViewById(R.id.fm_a_item_imgview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), ContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Fragment", "A");
                        bundle.putString("title", fm_a_item_title.getText().toString());
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }
                });

            }
        }

    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buyrecord,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("go",String.valueOf(item.getItemId()));
        switch (item.getItemId()){
            case 2131558556:
                Log.e("go","item");
//                adapter.removeItem(1);
//                recyclerView_fm_a.swapAdapter(adapter,false);
//                Log.e("view",String.valueOf(recyclerView_fm_a.getChildItemId(view)));
                break;
        }
        return true;
    }

    private FragmentInteraction listterner;

    public FragmentA() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listterner = (FragmentInteraction) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listterner = null;
    }


    public interface FragmentInteraction {
        void process(View str);
    }


    @Override
    public void onResume() {
        super.onResume();
        kklist = KKfavorite.getFavorite();
        ContentA adapter = new ContentA(kklist, view);
        recyclerView_fm_a.swapAdapter(adapter, false);
    }


}
