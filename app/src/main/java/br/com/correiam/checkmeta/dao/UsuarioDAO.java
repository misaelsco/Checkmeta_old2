package br.com.correiam.checkmeta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import br.com.correiam.checkmeta.dominio.Usuario;

/**
 * Created by Misael Correia on 19/05/15.
 * misaelsco@gmail.com
 */
public class UsuarioDAO {

    private SQLiteHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_USER = "User";

    public UsuarioDAO(){

    }
    public UsuarioDAO(Context context){
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    //Transforma uma conta em um mapa (ContentValues) e
    //usa o recurso da classe db (metodo insert)
    public Long insert(Usuario user){
        open();
        Long insertedId = Long.valueOf(-1);

        ContentValues values = new ContentValues();
        values.put("name",user.getNome());
        values.put("password",user.getSenha());
        values.put("email", user.getEmail());
        if(user.getIsFacebookUser())
            values.put("isFacebookUser", 1);
        else
            values.put("isFacebookUser", 0);

        try{
            insertedId = db.insert(TABLE_USER,null,values);
        }catch (SQLiteException ex){
            close();
        }
        close();
        return insertedId;
    }

    public Usuario getUsuarioById(Long idUser){
        Usuario user = null;
        open();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE id = '" + idUser+ "'", null);

            if(c.getCount() > 0) {
                user = new Usuario();
                c.moveToFirst();
                user.setId(c.getLong(c.getColumnIndex("id")));
                user.setNome(c.getString(c.getColumnIndex("name")));
                user.setEmail(c.getString(c.getColumnIndex("email")));
                user.setSenha(c.getString(c.getColumnIndex("password")));
                int isFacebookUser = c.getInt(c.getColumnIndex("isFacebookUser"));
                if(isFacebookUser == 1) {
                    user.setIsFacebookUser(true);
                }
                else
                {
                    user.setIsFacebookUser(false);
                }
            }
            c.close();
        }
        catch (SQLiteException ex){
            Log.d("LogError", ex.toString());
            close();
        }
        close();
        return user;
    }


    public Long validaCredenciais(String email, String password)
    {
        Long idUser = -1L;
        open();
        try {
            Cursor c = db.rawQuery("SELECT id FROM " + TABLE_USER + " WHERE email = '" + email + "' AND password = '" + password + "'", null);

            if(c.getCount() > 0) {
                //user = new Usuario();
                c.moveToFirst();
                idUser = c.getLong(c.getColumnIndex("id"));
                //user.setNome(c.getString(c.getColumnIndex("name")));
                //user.setEmail(c.getString(c.getColumnIndex("email")));
                //user.setSenha(c.getString(c.getColumnIndex("password")));
                //user.setIsFacebookUser(false);
            }
            c.close();
        }
        catch (SQLiteException ex){
            Log.d("LogError", ex.toString());
            close();
        }
        close();
        return idUser;
    }

    //Verifica se o email já possui algum cadastro
    public Long ehDuplicado(String email) {
        open();
        try {
            Cursor c = db.rawQuery("SELECT id FROM " + TABLE_USER + " WHERE email = '" + email + "'", null);
            if (c.getCount() > 0) {

                c.moveToFirst();
                return c.getLong(c.getColumnIndex("id"));
            }
        } catch (SQLiteException ex) {
            Log.d("LogError", "Email '" + email + "' é duplicado [" + ex.toString() + "]");
            close();
        }
        close();
        return -1L;
    }

    public boolean update(Usuario user){

        open();
        Long insertedId = Long.valueOf(-1);

        ContentValues values = new ContentValues();
        values.put("name",user.getNome());
        values.put("password",user.getSenha());
        values.put("email", user.getEmail());
        values.put("isFacebookUser", 0);

        try{
            Log.d("DEBUG", "Update: " + db.update(TABLE_USER, values, "_id=" + user.getId(),null));
        }catch (SQLiteException ex){
            close();
        }
        close();
        return true;
    }
}
