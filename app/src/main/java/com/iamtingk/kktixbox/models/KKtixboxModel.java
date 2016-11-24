package com.iamtingk.kktixbox.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.From;

import java.util.List;

/**
 * Created by tingk on 2016/6/11.
 */
@Table(name = "kboxs", id = "_id")
public class KKtixboxModel extends Base {
    @Column(name = "kid")
    public int kid;
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

    public KKtixboxModel() {
        super();
    }



    public static List<KKtixboxModel> kklist() {
        return new Select()
                .from(KKtixboxModel.class)
                .limit(100)
                .orderBy("kid ASC")
                .execute();
    }


    public static KKtixboxModel KKcontent(String titleB){
        return new Select()
                .from(KKtixboxModel.class)
                .where("title=?",titleB)
                .executeSingle();
    }

    //刪除資料表跟重置id
    public static void truncate(Class<? extends Model> type) {
        TableInfo tableInfo = Cache.getTableInfo(type);
        ActiveAndroid.execSQL("delete from " + tableInfo.getTableName() + ";");
        ActiveAndroid.execSQL("delete from sqlite_sequence where name='" + tableInfo.getTableName() + "';");

    }

}
