package com.iamtingk.kktixbox.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tingk on 2016/6/16.
 */
@Table(name = "kfavorite", id = "_id")
public class KKfavorite extends Base {
    @Column(name = "fid")
    public long fid;
    @Column(name = "url")
    public String url;
    @Column(name = "published")
    public String published;
    @Column(name = "title")
    public String title;
    @Column(name = "summary")
    public String summary;
    @Column(name = "content")
    public String content;
    @Column(name = "author_name")
    public String author_name;
    @Column(name = "author_uri")
    public String author_uri;
    @Column(name = "CreatedTime")
    public long created_time;

    public KKfavorite(){
        super();
    }

    public static List<KKfavorite> getFavorite(){
        return new Select()
                .from(KKfavorite.class)
                .orderBy("CreatedTime DESC")
                .execute();
    }

    public static KKfavorite getFavoritetitle(String title){
        return new Select()
                .from(KKfavorite.class)
                .orderBy("CreatedTime DESC")
                .where("title=?",title)
                .executeSingle();
    }

    //比對title 列出單一項
    public static KKfavorite eqFa(String str){
        return new Select()
                .from(KKfavorite.class)
                .where("title=?",str)
                .executeSingle();
    }
    //刪除單一項
    public static List<KKfavorite> KKdelete(String str){
        return new Delete()
                .from(KKfavorite.class)
                .where("title=?",str)
                .execute();
    }

    //刪除指定position
//    public static List<KKfavorite> deleteFavorite(int position){
//        return new Select()
//                .from(KKfavorite.class)
//                .orderBy("CreatedTime DESC")
//                .where("",position)
//                .execute();
//    }


}
