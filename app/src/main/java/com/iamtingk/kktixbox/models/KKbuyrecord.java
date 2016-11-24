package com.iamtingk.kktixbox.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tingk on 2016/6/19.
 */
@Table(name = "kbuyrecord", id = "_id")
public class KKbuyrecord extends Base {
    @Column(name = "bid")
    public long bid;
    @Column(name = "url")
    public String url;
    @Column(name = "published")
    public String published;
    @Column(name = "title")
    public String title;
    @Column(name = "CreatedTime")
    public long created_time;

    public KKbuyrecord(){
        super();
    }

    public static List<KKbuyrecord> getbuyList(){
        return new Select()
                .from(KKbuyrecord.class)
                .orderBy("CreatedTime DESC")
                .execute();
    }

    public static KKbuyrecord eq(String title){
        return new Select()
                .from(KKbuyrecord.class)
                .where("title=?",title)
                .executeSingle();
    }
    public static List<KKbuyrecord> deletebuy(){
        return new Delete()
                .from(KKbuyrecord.class)
                .execute();
    }
}