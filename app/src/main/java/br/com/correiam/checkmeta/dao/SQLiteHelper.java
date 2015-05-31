package br.com.correiam.checkmeta.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Misael Correia on 16/05/15.
 * misaelsco@gmail.com
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "checkmeta.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getTableUser());
        db.execSQL(getTableMeta());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableUser());
        db.execSQL(dropTableMeta());
    }

    private String getTableUser(){
        return "CREATE TABLE User(" +
                "id integer primary key autoincrement," +
                "name text not null," +
                "email text not null," +
                "password text,"+
                "isFacebookUser integer not null);";
    }

    private String getTableMeta(){
        return "CREATE TABLE Meta(" +
                "id integer primary key autoincrement," +
                "name text not null, "+
                "description text not null," +
                "dueDate text not null," +
                "state text, " +
                "actualDate text, "+
                "idUser integer not null)";
    }

    private String dropTableUser(){
        return "DROP TABLE User;";
    }

    private String dropTableMeta(){ return "DROP TABLE Meta"; }
}