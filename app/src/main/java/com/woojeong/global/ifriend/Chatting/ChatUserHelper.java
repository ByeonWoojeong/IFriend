package com.woojeong.global.ifriend.Chatting;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatUserHelper extends SQLiteOpenHelper {

    public ChatUserHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE CHATUSER (_id INTEGER PRIMARY KEY AUTOINCREMENT,MIDX TEXT NOT NULL UNIQUE,ROOM TEXT,USERNAME TEXT);");
        } catch (SQLException e){
            Log.i("ChatUserHelper", "DB create e : " + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("ChatUserHelper", " " + String.format("onUpgrade oldVersion : %d, newVersion : %d", oldVersion, newVersion));
        try {
            sqLiteDatabase.execSQL("drop table university");
            onCreate(sqLiteDatabase);
        } catch (SQLException e){
            Log.i("ChatUserHelper", "DB upgrade e : " + e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }
}
