package br.com.correiam.checkmeta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import br.com.correiam.checkmeta.dominio.Meta;

/**
 * Created by Misael Correia on 27/05/2015.
 * misaelsco@gmail.com
 */
public class MetaDAO {
    private SQLiteHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_META = "Meta";

    public MetaDAO(){

    }
    public MetaDAO(Context context){
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Long insert(Meta meta){
        open();
        Long insertedId = Long.valueOf(-1);

        ContentValues values = new ContentValues();
        values.put("name",meta.getNome());
        values.put("description",meta.getDescricao());
        values.put("dueDate", meta.getDueDate());
        values.put("idUser", meta.getIdUser());

        try{
            insertedId = db.insert(TABLE_META,null,values);
        }catch (SQLiteException ex){
            close();
        }
        close();
        return insertedId;
    }
}
