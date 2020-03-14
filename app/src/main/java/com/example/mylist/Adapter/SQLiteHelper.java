package com.example.mylist.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class SQLiteHelper extends SQLiteOpenHelper{
    final String CreateTime = "00:00:00";
    final String Importance = "0";
    final String slogan = " ";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void CreateItemTable(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS Item(Id INTEGER PRIMARY KEY AUTOINCREMENT, ItemName VARCHAR, Context VARCHAR, CreateTime VARCHAR, RemindTime VARCHAR, Importance VARCHAR, UserName VARCHAR)";
        database.execSQL(sql);
    }

    public void insertItemData(String ItemName, String Context,  String RemindTime, String UserName){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Item VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, ItemName);
        statement.bindString(2, Context);
        statement.bindString(3, CreateTime);
        statement.bindString(4, RemindTime);
        statement.bindString(5, Importance);
        statement.bindString(6, UserName);

        statement.executeInsert();
    }

    public void updateItemData(String ItemName, String Context, String RemindTime,int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE Item SET ItemName = ?,Context = ?,RemindTime=? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, ItemName);
        statement.bindString(2, Context);
//        statement.bindString(3, CreateTime);
        statement.bindString(3, RemindTime);
//        statement.bindString(5, Importance);
//        statement.bindString(6, UserName);
        statement.bindDouble(4, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteItemData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM Item WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void CreateUserTable(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT, UserName VARCHAR, UserPassword VARCHAR, UserEmail VARCHAR,UserImage VARCHAR ,UserSlogan VARCHAR)";
        database.execSQL(sql);
    }

    public void insertUserData(String UserName, String UserPassword, String UserEmail,int UserImage){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO User VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, UserName);
        statement.bindString(2, UserPassword);
        statement.bindString(3, UserEmail);
        statement.bindDouble(4, (double) UserImage);
        statement.bindString(5,slogan);
        statement.executeInsert();
    }

    public void insertUserData(String UserName, String UserPassword, String UserEmail,int UserImage,String ss){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO User VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, UserName);
        statement.bindString(2, UserPassword);
        statement.bindString(3, UserEmail);
        statement.bindDouble(4, (double) UserImage);
        statement.bindString(5, ss);
        statement.executeInsert();
    }
    public void updateUserData(int id,String UserName, String UserEmail,int UserImage,String UserSlogan) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE User SET  UserName= ?,UserEmail=?,UserImage=?,UserSlogan=? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, UserName);
        statement.bindString(2, UserEmail);
        statement.bindDouble(3, (double) UserImage);
        statement.bindString(4, UserSlogan);
        statement.bindDouble(5,(double) id);

        statement.execute();
        database.close();
    }

    public  void deleteUserData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM Item WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }


    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
