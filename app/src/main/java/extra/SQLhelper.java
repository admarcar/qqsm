package extra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by admarcar on 19/02/2018.
 */

public class SQLhelper extends SQLiteOpenHelper{

    private static SQLhelper helper;

    public static SQLhelper getInstance(Context context){
        if(helper == null){
            helper = new SQLhelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE score_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, score INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private SQLhelper(Context context){
        super(context, "qqsm_database", null, 1);
    }

    public void putScore(String username, int score){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("name", username);
        content.put("score", score);
        db.insert("score_table", null, content);
        db.close();
    }

    public void removeScore(String name, String score){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("score_table", "name=? and score=?", new String[]{name,score});
        db.close();
    }

    public void removeScores(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("score_table", null, null);
        db.close();
    }

    public ArrayList<HighScore> getScores(){
        ArrayList<HighScore> list = new ArrayList<HighScore>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT name, score FROM score_table", null);

        while(cursor1.moveToNext()){
            HighScore ps = new HighScore(cursor1.getString(0), cursor1.getString(1),"","");
            list.add(ps);
        }
        cursor1.close();
        db.close();
        Collections.sort(list);
        return list;
    }
}
