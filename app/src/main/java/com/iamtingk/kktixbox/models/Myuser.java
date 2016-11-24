package com.iamtingk.kktixbox.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by tingk on 2016/7/12.
 */
@Table(name = "myuser")
public class Myuser extends Base {
    @Column(name = "name")
    public String name;

    @Column(name = "phone")
    public String phone;

    @Column(name = "fbid")
    public String fbid;

    @Column(name = "sms_verify")
    public String sms_verify;

    @Column(name = "sms_token")
    public String sms_token;

    @Column(name = "gcm_token")
    public String gcm_token;

    public Myuser(){
        super();
    }

    public static Myuser getuser(String phone){
        return new Select()
                .from(Myuser.class)
                .where("phone=?",phone)
                .executeSingle();
    }
    public static Myuser userif(){
        return new Select()
                .from(Myuser.class)
                .executeSingle();
    }
    public static List<Myuser> getuserall(){
        return new Select()
                .from(Myuser.class)
                .execute();
    }
    public static void truncate(Class<? extends Model> type) {
        TableInfo tableInfo = Cache.getTableInfo(type);
        ActiveAndroid.execSQL("delete from " + tableInfo.getTableName() + ";");
        ActiveAndroid.execSQL("delete from sqlite_sequence where name='" + tableInfo.getTableName() + "';");

    }
}
